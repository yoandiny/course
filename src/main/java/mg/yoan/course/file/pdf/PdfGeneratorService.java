package mg.yoan.course.file.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import lombok.SneakyThrows;
import mg.yoan.course.PojaGenerated;
import org.springframework.stereotype.Service;

@PojaGenerated
@Service
public class PdfGeneratorService {

  @SneakyThrows
  public File generatePdf(String htmlContent, String filenamePrefix) {
    File tempFile = Files.createTempFile(filenamePrefix, ".pdf").toFile();
    try (OutputStream os = new FileOutputStream(tempFile)) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.withHtmlContent(htmlContent, null);
      builder.toStream(os);
      builder.run();
    }
    return tempFile;
  }
}
