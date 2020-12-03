package nuist.zjl.algorithm.distanceMatrix;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import nuist.zjl.utils.POIUtils;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
/**
 * Bray-Curtis矩阵求解类
 */
public class BrayCurtis {
    /**
     * 计算两个处理间的Bray-Curtis距离
     * @param siteA A处理的OTU数目列表
     * @param siteB B处理的OTU数目列表
     * @return 返回处理A与处理B微生物群落的Bray-Curtis距离值
     * @throws Exception
     */
    private BigDecimal calculateDistanceValue(List<BigDecimal> siteA, List<BigDecimal> siteB) throws Exception {
        if (siteA.size() != siteB.size()) throw new Exception("两组数据OTU数量不同");
        BigDecimal sumOfMin = new BigDecimal(0);  // 对应公式中 ∑ min(S_(A,i),S_(B,i))
        BigDecimal sumOfSiteA = new BigDecimal(0);  // 对应公式中 ∑ S_(A,i)
        BigDecimal sumOfSiteB = new BigDecimal(0);  // 对应公式中 ∑ S_(B,i)
        for (int i = 0; i < Math.min(siteA.size(), siteB.size()); i++) {
            BigDecimal numOfOTUA = siteA.get(i);
            BigDecimal numOfOTUB = siteB.get(i);
            sumOfMin = sumOfMin.add(numOfOTUA.min(numOfOTUB));
            sumOfSiteA = sumOfSiteA.add(numOfOTUA);
            sumOfSiteB = sumOfSiteB.add(numOfOTUB);
        }
        BigDecimal firstStep = sumOfMin.divide(sumOfSiteA.add(sumOfSiteB), new MathContext(10)).multiply(new BigDecimal(2));
        BigDecimal brayCurtisDistance = new BigDecimal(1).subtract(firstStep);
        return brayCurtisDistance;
    }

    /**
     * 将OTU列表中数据转换为BigDecimal类型，保证计算精度
     * @param list
     * @return OTU数目列表
     */
    private List<BigDecimal> getBigDecimalList(List<String> list) {
        List<BigDecimal> result = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            result.add(new BigDecimal(list.get(i)));
        }
        return result;
    }

    /**
     * 计算Bray-Curtis距离，并整理为距离矩阵
     * @param columnList
     * @return Bray-Curtis距离矩阵
     * @throws Exception
     */
    private List<List<String>> getDistanceMatrix(List<List<String>> columnList) throws Exception {
        List<List<String>> result = new ArrayList<>();
        List<String> firstLine = new ArrayList<>();
        firstLine.add("x1");
        for (int i = 1; i < columnList.size(); i++) {
            firstLine.add(columnList.get(i).get(0));
        }
        result.add(firstLine);
        for (int i = 1; i < columnList.size(); i++) {
            List<String> resultLine = new ArrayList<>();
            String groupName = columnList.get(i).get(0);
            resultLine.add(groupName);
            for (int j = 1; j < columnList.size(); j++) {
                List<BigDecimal> list1 = getBigDecimalList(columnList.get(i));
                List<BigDecimal> list2 = getBigDecimalList(columnList.get(j));
                BigDecimal distanceValue = calculateDistanceValue(list1, list2);
                resultLine.add(distanceValue.toString());
            }
            result.add(resultLine);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        //获取列数据列表
        List<List<String>> colList = POIUtils.readDataByCol("C:\\Users\\36085\\Desktop\\microbial_sci\\raw_figures\\PCA\\JS_B\\jsb_otus.xlsx");
        BrayCurtis brayCurtis = new BrayCurtis();
        List<List<String>> resultList = brayCurtis.getDistanceMatrix(colList);
        XSSFWorkbook workbook = POIUtils.getWorkBookFromList(resultList);
        POIUtils.getExcelFileFromWorkbook(workbook, new File("C:\\Users\\36085\\Desktop\\microbial_sci\\raw_figures\\PCoA\\jsb_distanceMatrix.xlsx"));
    }
}
