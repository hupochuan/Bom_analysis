package indi.ycl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPdf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pdfFile = new File("D:/项目文件/物料分析/海达股份：2016年年度报告.pdf");
      PDDocument document = null;
      try
      {
          // 方式一：
          /**
          InputStream input = null;
          input = new FileInputStream( pdfFile );
          //加载 pdf 文档
          PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
          parser.parse();
          document = parser.getPDDocument();
          **/

          // 方式二：
          document=PDDocument.load(pdfFile);

          // 获取页码
          int pages = document.getNumberOfPages();

          // 读文本内容
          PDFTextStripper stripper=new PDFTextStripper();
          // 设置按顺序输出
          stripper.setSortByPosition(true);
          stripper.setStartPage(1);
          stripper.setEndPage(pages);
          String content = stripper.getText(document);
          System.out.println(content);     
      }
      catch(Exception e)
      {
          System.out.println(e);
      }


	}

}
