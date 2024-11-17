package ct553.backend.exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.statistic.StatisticTimeData.StatisticFilterPeriod;
import ct553.backend.user.UserService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/export")
public class ProductExporterResource {
    
    @Autowired
    ProductExporterService petProductImporterService;

    @Autowired
    UserService userService;

    @GetMapping("/{period}")
    public void exportToExcel(@PathVariable("period") StatisticFilterPeriod period, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=reports_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        petProductImporterService.exportByPeriod(period, response);
    }

}
