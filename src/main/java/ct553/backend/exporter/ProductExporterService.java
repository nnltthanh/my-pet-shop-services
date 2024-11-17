package ct553.backend.exporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.product.pet.PetProduct;
import ct553.backend.product.service.PetCustomerServiceProduct;
import ct553.backend.product.service.ServiceProduct;
import ct553.backend.statistic.DateRevenueReport;
import ct553.backend.statistic.StatisticService;
import ct553.backend.statistic.StatisticTimeData.StatisticFilterPeriod;
import ct553.backend.statistic.StatisticTimeDataReport;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ProductExporterService {

    @Autowired
    StatisticService statisticService;
    
    private XSSFWorkbook workbook;
    
    private XSSFSheet sheet;

    private void writeHeaderLineForOrderSheet() {
        sheet = workbook.createSheet("Đơn hàng đã bán");
         
        Row row = sheet.createRow(0);
        CellStyle style = createCellStyleForHeader();
        
        createCell(row, 0, "ID đơn hàng", style);      
        createCell(row, 1, "Khách hàng", style);
        createCell(row, 2, "Chi tiết đơn hàng", style);
        createCell(row, 3, "Tổng tiền", style);    
        createCell(row, 4, "Ngày tạo đơn", style);       
        createCell(row, 5, "Nhân viên xử lý", style);
        createCell(row, 6, "Trạng thái", style);
        createCell(row, 7, "Ghi chú", style);
    }

    private void writeHeaderLineForProductSheet() {
        sheet = workbook.createSheet("Sản phẩm đã bán");
         
        Row row = sheet.createRow(0);
        CellStyle style = createCellStyleForHeader();

        createCell(row, 0, "ID sản phẩm", style);      
        createCell(row, 1, "Tên sản phẩm", style);
        createCell(row, 2, "Số lượng bán", style);
        createCell(row, 3, "Tổng thanh toán", style);    
        createCell(row, 4, "Ngày bán", style);
        createCell(row, 5, "Loại thú nuôi", style);       
        createCell(row, 6, "Giống thú nuôi", style);
    }
     
    private void writeHeaderLineForServiceSheet() {
        sheet = workbook.createSheet("Dịch vụ sử dụng");
         
        Row row = sheet.createRow(0);
        CellStyle style = createCellStyleForHeader();

        createCell(row, 0, "ID dịch vụ", style);      
        createCell(row, 1, "Tên dịch vụ", style);
        createCell(row, 2, "Loại dịch vụ", style);
        createCell(row, 3, "Tổng thanh toán", style);    
        createCell(row, 4, "Ngày đăng ký", style);
        createCell(row, 5, "Thời gian phục vụ", style);
        createCell(row, 6, "Chi tiết dịch vụ", style);       
        createCell(row, 7, "Ghi chú", style);
    }

    private void writeHeaderLineForRevenueSheet() {
        sheet = workbook.createSheet("Doanh thu đạt được");
         
        Row row = sheet.createRow(0);
        CellStyle style = createCellStyleForHeader();

        createCell(row, 0, "Ngày tính", style);      
        createCell(row, 1, "Tổng doanh thu", style);
    }

    private CellStyle createCellStyleForHeader() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
     
    private CellStyle createCellStyleForDataLine() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } 
        // else if (value instanceof BigDecimal price) {
        //     cell.setCellValue(price.doubleValue());

        // } 
        else if (value instanceof LocalDate date) {
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(12);
            cellStyle.setFont(font);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);

            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"));
            cell.setCellValue((LocalDate)date);
            cell.setCellStyle(cellStyle);
        } else if (!(value instanceof BigDecimal)) {
            cell.setCellValue((String) value);
        }
        if (!(value instanceof LocalDate) && !(value instanceof BigDecimal)) {
            cell.setCellStyle(style);
        }

        if (value instanceof BigDecimal) {
            cell.setCellValue(((Number)value).doubleValue());
            CellStyle currencyStyle = createCellStyleForDataLine();
            // #,##0 ₫ -> 70,000 ₫
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0 ₫"));
            cell.setCellStyle(currencyStyle);
        }

    }
     
    private void writeDataLinesForOrderSheet(List<Order> orders) {
        int rowCount = 1;
        CellStyle style = createCellStyleForDataLine();
                 
        for (Order order : orders) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, order.getId(), style);
            createCell(row, columnCount++, "[" + order.getCustomer().getId().toString() +"] " + order.getCustomer().getName(), style);

            StringBuilder orderDetails = new StringBuilder();
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail != null) {
                    Locale locale = new Locale("vi", "VN");
                    Currency currency = Currency.getInstance("VND");

                    DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
                    df.setCurrency(currency);
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                    numberFormat.setCurrency(currency);

                    orderDetails.append(orderDetail.getProductDetail().getProduct().getName() + " - x" + orderDetail.getQuantity() + " - [" + numberFormat.format(orderDetail.getTotal().doubleValue()) + "];\n");
                }
            }
            createCell(row, columnCount++, orderDetails.toString(), style);
            createCell(row, columnCount++, order.getTotal(), style);
            createCell(row, columnCount++, order.getCreateDate(), style);
            createCell(row, columnCount++, order.getEmployee() != null ? order.getEmployee().getName() : "", style);
            createCell(row, columnCount++, order.getStatus() != null ? order.getStatus().getVietnameseValue() : "", style);
            createCell(row, columnCount++, order.getNote(), style);
        }
    }

    private void writeDataLinesForRevenueSheet(List<DateRevenueReport> revenues) {
        int rowCount = 1;
        CellStyle style = createCellStyleForDataLine();
                 
        for (DateRevenueReport revenueReport : revenues) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, revenueReport.getDate(), style);
            createCell(row, columnCount++, revenueReport.getRevenue(), style);
        }
    }

    private void writeDataLinesForProductSheet(List<OrderDetail> orders) {
        int rowCount = 1;
        CellStyle style = createCellStyleForDataLine();
                 
        for (OrderDetail order : orders) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, order.getProductDetail().getProduct().getId(), style);
            createCell(row, columnCount++, order.getProductDetail().getProduct().getName(), style);
            createCell(row, columnCount++, order.getQuantity(), style);
            createCell(row, columnCount++, order.getTotal(), style);
            createCell(row, columnCount++, order.getCreateDate(), style);
            createCell(row, columnCount++, ((PetProduct) order.getProductDetail().getProduct()).getCategory().getBreed().getVietnameseValue(), style);
            createCell(row, columnCount++, ((PetProduct) order.getProductDetail().getProduct()).getCategory().getName(), style);
        }
    }

    private void writeDataLinesForServiceSheet(List<PetCustomerServiceProduct> petCustomerServiceProducts) {
        int rowCount = 1;
        CellStyle style = createCellStyleForDataLine();
                 
        for (PetCustomerServiceProduct petCustomerServiceProduct : petCustomerServiceProducts) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            Order order = petCustomerServiceProduct.getOrder();

            StringBuilder orderDetails = new StringBuilder();
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail != null && orderDetail.getProductDetail().getPetServiceVariant() != null) {
                    orderDetails.append(
                        orderDetail.getProductDetail().getPetServiceVariant().getVariantName().getVietnameseValue() + 
                        " - " + orderDetail.getProductDetail().getPetServiceVariant().getVariantValue() + "; \n");
                }
            }

            if (order.getOrderDetails() != null && order.getOrderDetails().size() > 0) {

                StringBuilder serveTime = new StringBuilder();
                serveTime.append(petCustomerServiceProduct.getServeFrom().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
                serveTime.append(" - ");
                serveTime.append(petCustomerServiceProduct.getServeTo().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));

                createCell(row, columnCount++, order.getOrderDetails().get(0).getProductDetail().getProduct().getId(), style);
                createCell(row, columnCount++, order.getOrderDetails().get(0).getProductDetail().getProduct().getName(), style);
                createCell(row, columnCount++, ((ServiceProduct)order.getOrderDetails().get(0).getProductDetail().getProduct()).getType().getVietnameseValue(), style);
                createCell(row, columnCount++, order.getTotal(), style);
                createCell(row, columnCount++, order.getOrderDetails().get(0).getCreateDate(), style);
                createCell(row, columnCount++, serveTime.toString(), style);
                createCell(row, columnCount++, orderDetails.toString(), style);
                createCell(row, columnCount++, petCustomerServiceProduct.getCustomerNote(), style);
            } else {
                rowCount--;
            }
             
        }
    }
     
    public void exportByPeriod(StatisticFilterPeriod period, HttpServletResponse response) throws IOException {
        StatisticTimeDataReport dataReport = this.statisticService.getReportStatisticByFilter(period);
        this.workbook = new XSSFWorkbook();

        writeHeaderLineForRevenueSheet();
        writeDataLinesForRevenueSheet(dataReport.getRevenuesByPeriods());

        writeHeaderLineForOrderSheet();
        writeDataLinesForOrderSheet(dataReport.getOrdersByPeriods());

        writeHeaderLineForProductSheet();
        writeDataLinesForProductSheet(dataReport.getProductsByPeriods());

        writeHeaderLineForServiceSheet();
        writeDataLinesForServiceSheet(dataReport.getServicesByPeriods());

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
    }


}
