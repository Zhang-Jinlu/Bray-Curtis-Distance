package nuist.zjl.utils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于Apache POI进行Excel表格的读取和写入
 */
public class POIUtils {
    /**
     * 按列读取Excel数据
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<List<String>> readDataByCol(String filePath) throws IOException {
        XSSFSheet sheet = getSheet(filePath, 0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        int lastNumOfCol = sheet.getRow(0).getLastCellNum() - 1;
        List<List<String>> result = new ArrayList<>();
        for (int colNum = 0; colNum <= lastNumOfCol; colNum++) {
            List<String> column = new ArrayList<>();
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                column.add(sheet.getRow(rowNum).getCell(colNum).toString());
            }
            result.add(column);
        }
        return result;
    }

    /**
     * 获取Excel工作表对象
     * @param filePath
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public static XSSFSheet getSheet(String filePath, Integer sheetIndex) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(filePath);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        return sheet;
    }

    /**
     * 根据距离矩阵List创建Excel工作表对象
     * @param rowList
     * @return
     */
    public static XSSFWorkbook getWorkBookFromList(List<List<String>> rowList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("newSheet");
        int rowNum = 0;
        for (List<String> rowData : rowList) {
            XSSFRow row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.size(); i++) {
                row.createCell(i).setCellValue(rowData.get(i));
            }
        }
        return workbook;
    }

    /**
     * 将工作表对象保存为指定路径的.xlsx文件
     * @param workbook
     * @param excelFile
     * @throws IOException
     */
    public static void getExcelFileFromWorkbook(XSSFWorkbook workbook, File excelFile) throws IOException {
        FileOutputStream out = new FileOutputStream(excelFile);
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
    }
}
