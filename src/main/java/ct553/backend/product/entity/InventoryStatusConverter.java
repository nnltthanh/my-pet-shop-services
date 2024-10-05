package ct553.backend.product.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InventoryStatusConverter implements AttributeConverter<InventoryStatus, String> {

    @Override
    public String convertToDatabaseColumn(InventoryStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public InventoryStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return InventoryStatus.from(dbData);
    }
}

