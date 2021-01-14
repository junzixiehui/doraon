package com.junzixiehui.doraon.orm;

import org.teasoft.bee.osql.*;
import org.teasoft.bee.osql.service.ObjSQLRichService;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/5/16  10:59 PM
 * @version: 1.0
 */
public class OrmTest {

	public static void main(String[] args) {

		Suid suid=BeeFactory.getHoneyFactory().getSuid();
		final SuidRich suidRich = BeeFactory.getHoneyFactory().getSuidRich();
		final MoreTable moreTable = BeeFactory.getHoneyFactory().getMoreTable();
		final ObjToSQLRich objToSQLRich = BeeFactory.getHoneyFactory().getObjToSQLRich();

		//需要先生成相应的Javabean
		/*Orders orders1=new Orders();
		orders1.setId(100001L);
		orders1.setName("Bee(ORM Framework)");

		//默认不处理null和空字符串.不用再写一堆的判断;其它有值的字段全部自动作为过滤条件
		List<Orders> list1 =suid.select(orders1);  //select
		for (int i = 0; i < list1.size(); i++) {
			System.out.println(list1.get(i).toString());
		}

		orders1.setName("Bee--ORM Framework");
		//默认只更新需要更新的字段. 过滤条件默认只用id字段,其它需求可用SuidRich中的方法.
		int updateNum=suid.update(orders1);   //update
		System.out.println("update record:"+updateNum);

		Orders orders2=new Orders();
		orders2.setUserid("bee");
		orders2.setName("Bee(ORM Framework)");
		orders2.setTotal(new BigDecimal(91.99));
		orders2.setRemark("");  //empty String test

		//默认不处理null和空字符串.不用再写一堆的判断;其它有值的字段全部自动插入数据库中.
		//方便结合DB插值,如id自动增长,由DB插入;createtime由DB默认插入
		int insertNum=suid.insert(orders2); //insert
		System.out.println("insert record:"+insertNum);

		//默认不处理null和空字符串.不用再写一堆的判断;其它有值的字段全部自动作为过滤条件
		//		int deleteNum=suid.delete(orders2);   //delete
		//		System.out.println("delete record:"+deleteNum);

		List<Orders> list2 =suid.select(orders1); //select  confirm the data
		for (int i = 0; i < list2.size(); i++) {
			System.out.println(list2.get(i).toString());
		}*/

		Condition condition=new ConditionImpl();
		condition.op("userid", Op.eq, "bee%") //模糊查询
				//.orderBy(,OrderType.ASC)
				.between("total", 90, 100)     //范围查询
				.between("createtime","2020-03-01","2020-03-03")
				.orderBy("userid",OrderType.ASC) //排序
				.start(0).size(10);


	}

}
