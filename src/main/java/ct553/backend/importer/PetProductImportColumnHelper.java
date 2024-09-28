package ct553.backend.importer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class PetProductImportColumnHelper {
    
    private static Map<ImportColumnName, Integer> IMPORT_COLUMN_NAME_INDEX_MAP = new HashMap<>();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum ImportColumnName {
        PRODUCT_NAME("Tên sản phẩm"),
        PRODUCT_IMAGE("Hình ảnh"),
        PRODUCT_QUANTITY("Số lượng"),
        PRODUCT_PRICE("Giá"),
        PRODUCT_CATEGORY_BREED("Loại thú nuôi"),
        PRODUCT_CATEGORY_NAME("Giống thú nuôi"),
        PRODUCT_GENDER("Giới tính"),
        PRODUCT_COLOR("Màu lông"),
        PRODUCT_AGE("Tuổi (tháng)"),
        PRODUCT_DOB("Ngày sinh"),
        PRODUCT_ORIGIN("Nguồn gốc"),
        PRODUCT_WEIGHT("Cân nặng"),
        PRODUCT_LENGTH("Chiều dài"),
        PRODUCT_VACCINATION("Tình trạng tiêm vaccine"),
        PRODUCT_DESCRIPTION("Mô tả");

        @Getter
        public String value;

        public static ImportColumnName from(String value) {
            return Stream.of(ImportColumnName.values())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        }

    }

    public void setIndex(ImportColumnName columnName, Integer index) {
        IMPORT_COLUMN_NAME_INDEX_MAP.put(columnName, index);
    }

    public Map<ImportColumnName, Integer> getColumnNameIndexMap() {
        return IMPORT_COLUMN_NAME_INDEX_MAP;
    }


}
