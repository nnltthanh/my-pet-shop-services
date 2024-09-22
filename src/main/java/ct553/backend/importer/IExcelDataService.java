package ct553.backend.importer;

import java.util.List;

import ct553.backend.product.entity.Product;

public interface IExcelDataService {

	List<Product> getExcelDataAsList();
	
	int saveExcelData(List<Product> products);
}
