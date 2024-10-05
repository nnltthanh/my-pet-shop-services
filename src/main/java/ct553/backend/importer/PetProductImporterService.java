package ct553.backend.importer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.Gender;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.importer.PetProductImportColumnHelper.ImportColumnName;
import ct553.backend.pet.entity.PetBreed;
import ct553.backend.pet.entity.PetCategory;
import ct553.backend.pet.healthrecord.HealthRecord;
import ct553.backend.product.boundary.PetProductService;
import ct553.backend.product.entity.PetProduct;
import jakarta.transaction.Transactional;

@Service
public class PetProductImporterService {

    private static final String PET_PRODUCT_SHEET_NAME = "PET";

    @Autowired
    PetProductImportColumnHelper petProductImportColumn;

    @Autowired
    PetProductService petProductService;

    @Transactional
    public void importProducts(MultipartFile file) throws IOException { // TODO - check case import null
        Sheet sheet = this.setColumnNames(file);
        Map<ImportColumnName, Integer> columnNameIndexMap = this.petProductImportColumn.getColumnNameIndexMap();
        List<PetProduct> petProducts = new ArrayList<>();
        int index = 0;
        for (Row row : sheet) {
            if (index == 0) {
                index++;
                continue;
            }
            PetProduct petProduct = new PetProduct();
            Cell petName = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_NAME));
            if (petName != null && StringUtils.isNotBlank(petName.getStringCellValue())) {
                petProduct.setName(petName.getStringCellValue());
            }

            Cell imageUrl = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_IMAGE));
            if (imageUrl != null && StringUtils.isNotBlank(imageUrl.getStringCellValue())) {
                String formattedImageUrl = convertToThumbnailLink(imageUrl.getStringCellValue());
                ImageData imageData = new ImageData();
                imageData.setImageUrls(formattedImageUrl);
                imageData.setType(ImageDataType.PRODUCT);
                petProduct.setImageData(imageData);
            }

            Cell quantity = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_QUANTITY));
            if (quantity != null) {
                petProduct.setQuantity((int)quantity.getNumericCellValue());
            }

            Cell price = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_PRICE));
            if (price != null) {
                petProduct.setPrice(new BigDecimal(price.getNumericCellValue()));
            }

            PetCategory petCategory = new PetCategory();
            Cell petBreed = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_CATEGORY_BREED));
            Cell petCategoryName = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_CATEGORY_NAME));

            if (petBreed != null && petCategoryName != null && StringUtils.isNotBlank(petBreed.getStringCellValue()) && StringUtils.isNotBlank(petCategoryName.getStringCellValue())) {
                String petBreedString = petBreed.getStringCellValue();
                String petCategoryNameString = petCategoryName.getStringCellValue();
                if (petBreedString.equalsIgnoreCase("Chó") || petBreedString.equalsIgnoreCase("Dog")) {
                    petCategory.setBreed(PetBreed.DOG);
                }
                else if (petBreedString.equalsIgnoreCase("Mèo") || petBreedString.equalsIgnoreCase("CAT")) {
                    petCategory.setBreed(PetBreed.CAT);
                }
                else if (petBreedString.equalsIgnoreCase("Hamster") || petBreedString.equalsIgnoreCase("Chuột Hamster")) {
                    petCategory.setBreed(PetBreed.HAMSTER);
                }
                petCategory.setName(petCategoryNameString);
                petProduct.setCategory(petCategory);
            }

            Cell gender = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_GENDER));
            if (gender != null) {
                petProduct.setGender(Gender.from(gender.getStringCellValue()));
            }

            Cell color = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_COLOR));
            if (color != null && StringUtils.isNotBlank(color.getStringCellValue())) {
                petProduct.setColor(color.getStringCellValue());
            }

            HealthRecord healthRecord = new HealthRecord();
            Cell weight = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_WEIGHT));
            if (weight != null) {
                healthRecord.setWeight((float)weight.getNumericCellValue());
            }

            Cell length = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_LENGTH));
            if (length != null) {
                healthRecord.setLength((float)length.getNumericCellValue());
            }

            Cell vaccination = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_VACCINATION));
            if (vaccination != null && StringUtils.isNotBlank(vaccination.getStringCellValue())) {
                healthRecord.setVaccination(vaccination.getStringCellValue());
            }
            
            Cell origin = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_ORIGIN));
            if (origin != null && StringUtils.isNotBlank(origin.getStringCellValue())) {
                petProduct.setOrigin(origin.getStringCellValue());
            }

            Cell dobCell = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_DOB));
            if (dobCell != null) {
                Date dob = dobCell.getDateCellValue();
                if (dob != null) {
                    petProduct.setDateOfBirth(dob.toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate());
    
                    long age = ChronoUnit.MONTHS.between(
                        petProduct.getDateOfBirth().withDayOfMonth(1),
                        LocalDate.now().withDayOfMonth(1));
                    healthRecord.setAge((float)age);
                }
            }

            if (healthRecord.getAge() == null || healthRecord.getAge() == 0) {
                Cell ageCell = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_AGE));
                if (ageCell != null) {
                    healthRecord.setAge((float) ageCell.getNumericCellValue());
                    if (petProduct.getDateOfBirth() == null) {
                        // e.g 2.5 months -> current date: 28/9 => DOB: 28/7
                        petProduct.setDateOfBirth(LocalDate.now().minusMonths((long) Math.floor(ageCell.getNumericCellValue())));  
                    }
                }
            }

            Cell description = row.getCell(columnNameIndexMap.get(PetProductImportColumnHelper.ImportColumnName.PRODUCT_DESCRIPTION));
            if (description != null && StringUtils.isNotBlank(description.getStringCellValue())) {
                petProduct.setDescription(description.getStringCellValue());
            }
            
            petProduct.getHealthRecord().add(healthRecord);
            petProducts.add(petProduct);
        }
        this.saveAndReturnPetProducts(petProducts);
    }

    public List<PetProduct> saveAndReturnPetProducts(List<PetProduct> petProducts) {
        return petProducts.stream().map(pet -> {
            try {
                return this.petProductService.add(pet, null);
            } catch (IOException e) {
                e.printStackTrace();
                return pet;
            }
        }).toList();
    }

    public Sheet setColumnNames(MultipartFile file) throws IOException {
        Workbook workbook = this.openWorkbook(file);
        Sheet sheet = workbook.getSheet(PET_PRODUCT_SHEET_NAME);
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            int index = 0;
            for (Cell cell : headerRow) {
                String cellValue = cell.getStringCellValue();
                this.petProductImportColumn
                        .setIndex(PetProductImportColumnHelper.ImportColumnName.from(cellValue), index);
                index++;
            }
        }
        return sheet;

    }

    public Workbook openWorkbook(MultipartFile file) throws IOException {
        try (InputStream fileInputStream = new BufferedInputStream(file.getInputStream())) {
            if (file.getOriginalFilename().toLowerCase()
                    .endsWith("xlsx")) {
                return new XSSFWorkbook(fileInputStream);
            } else if (file.getOriginalFilename().toLowerCase()
                    .endsWith("xls")) {
                return new HSSFWorkbook(fileInputStream);
            } else {
                throw new IllegalArgumentException("The specified file is not an Excel file");
            }
        } catch (OLE2NotOfficeXmlFileException | NotOLE2FileException e) {
            throw new IllegalArgumentException(
                    "The file format is not supported. Ensure the file is a valid Excel file.", e);
        }
    }

    public String convertToThumbnailLink(String originalLink) {
        if (originalLink.contains("/file/d/") && originalLink.contains("drive.google.com")) {
            String fileId = originalLink.split("/file/d/")[1].split("/")[0];
            String thumbnailLink = "https://drive.google.com/thumbnail?sz=w320&id=" + fileId;
            return thumbnailLink; 
        }
        return originalLink;
    }

}
