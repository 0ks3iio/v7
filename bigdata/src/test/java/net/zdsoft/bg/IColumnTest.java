package net.zdsoft.bg;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.manager.datasource.IColumn;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * IColumn转换为JSON字符串测试
 * @author ke_shen@126.com
 * @since 2018/4/9 下午3:08
 */
public class IColumnTest {

	@Test
	public void _single2JSONStr()  {
		IColumn iColumn = new IColumn("key", 1);
		iColumn.setValue("value");
		IColumn _iColumn = new IColumn("key2", 2);
		_iColumn.setValue("value2");
		System.out.println(iColumn.toJSONString());
		System.out.println(JSONObject.toJSONString(iColumn));

		List<IColumn> iColumns = new ArrayList<IColumn>(2) {
			{
				add(iColumn);
				add(_iColumn);
			}
		};
		System.out.println(IColumn.toJSONString(iColumns.toArray(new IColumn[iColumns.size()])));

		JSONArray jsonObject = JSONObject.parseArray("[{\"key\":\"value\"},{\"key2\":\"value2\"}]");
		System.out.println(jsonObject);
	}
}
