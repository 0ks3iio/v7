package net.zdsoft.framework.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

public class POIUtils {

	/**
	 * Excel 转为 HTML
	 * 
	 * @param fileName
	 * @param outputFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static String excelToHtml(String fileName, String outputFile) throws Exception {
		InputStream is = new FileInputStream(fileName);

		HSSFWorkbook excelBook = new HSSFWorkbook(is);

		ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

		ethc.setOutputColumnHeaders(false);
		ethc.setOutputRowNumbers(false);

		ethc.processWorkbook(excelBook);

		Document htmlDocument = ethc.getDocument();
		return extractHtml(htmlDocument, outputFile);
	}

	public static String extractHtml(Document htmlDocument, String outputFile)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException,
			IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "GBK");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();

		String htmlStr = new String(out.toByteArray());
//		htmlStr = StringUtils.replace(htmlStr, "<h2>Sheet1</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>Sheet2</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>Sheet3</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>Sheet</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>sheet1</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>sheet2</h2>", "");
//		htmlStr = StringUtils.replace(htmlStr, "<h2>sheet3</h2>", "");
		return writeFile(htmlStr, outputFile);
	}

	public static void main(String[] args) {}

	/**
	 * Word 转为 HTML
	 *
	 * @param fileName
	 * @param outputFile
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static String wordToHtml(String fileName, String outputFile)
			throws IOException, ParserConfigurationException, TransformerException {
		HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(fileName));

		WordToHtmlConverter wthc = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

		wthc.setPicturesManager(new PicturesManager() {

			@Override
			public String savePicture(byte[] bytes, PictureType pt, String string, float f, float f1) {
				return string;
			}

		});

		wthc.processDocument(wordDoc);

		List<Picture> pics = wordDoc.getPicturesTable().getAllPictures();
		if (null != pics && pics.size() > 0) {
			for (Picture pic : pics) {
				pic.writeImageContent(new FileOutputStream(pic.suggestFullFileName()));
			}
		}

		Document htmlDocument = wthc.getDocument();

		return extractHtml(htmlDocument, outputFile);
	}

	public static String writeFile(String content, String path) {
		if (StringUtils.isBlank(path))
			return content;
		FileOutputStream fos = null;
		BufferedWriter bw = null;

		File file = new File(path);

		try {
			fos = new FileOutputStream(file);

			bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.write(content);
		} catch (FileNotFoundException ex) {
		} catch (UnsupportedEncodingException ex) {
		} catch (IOException ex) {
		} finally {
			try {
				if (null != bw) {
					bw.close();
				}
				if (null != fos) {
					fos.close();
				}
			} catch (IOException ex) {
			}

		}

		return content;
	}
}
