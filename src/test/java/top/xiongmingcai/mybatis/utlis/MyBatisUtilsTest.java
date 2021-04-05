package top.xiongmingcai.mybatis.utlis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import junit.framework.TestCase;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import top.xiongmingcai.mybatis.entity.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatisUtilsTest extends TestCase {
    public void testOpenSession() {
        SqlSession sqlSession = MyBatisUtils.openSession();
        MyBatisUtils.closeSqlSession(sqlSession);
    }

    @Test
    public void testQueryUsers() {
        SqlSession sqlSession = null;

        try {
            sqlSession = MyBatisUtils.openSession();
            List<Teacher> list = sqlSession.selectList("teacher.queryUsers");
            System.out.println("size:" + list.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyBatisUtils.closeSqlSession(sqlSession);
        }

    }
    @Test
    public void testQueryUserById() {
        SqlSession sqlSession = null;

        try {
            sqlSession = MyBatisUtils.openSession();
            Teacher teacher = sqlSession.selectOne("teacher.queryUserById", 2);
            System.out.println("teacher = " + teacher.getName() + teacher.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyBatisUtils.closeSqlSession(sqlSession);
        }
    }
    @Test
    public void testQueryUserRangeId() {
        SqlSession sqlSession = null;

        try {
            sqlSession = MyBatisUtils.openSession();
            Map<Object, Object> param = new HashMap<>();
            param.put("min", 1);
            param.put("max", 2);
            param.put("limt", 10);

            List<Teacher> list = sqlSession.selectList("teacher.queryUserRangeId", param);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyBatisUtils.closeSqlSession(sqlSession);
        }
    }
    @Test
    public void testBatchInsert() {
        SqlSession sqlSession = null;

        try {
            sqlSession = MyBatisUtils.openSession();
            List<Teacher> list1 = new ArrayList<>();

            for (int i = 0; i < 500; i++) {
                Teacher teacher = new Teacher();
                teacher.setName("马化腾");
                teacher.setSex("男");
                teacher.setjNo(i+100);
                teacher.setSubject("CEO");
                teacher.setGrade("资本运作");
                teacher.setDescription("创业玩的是兴趣，而非改变命运。");
                list1.add(teacher);
            }
            sqlSession.insert("teacher.batchInsert", list1);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            MyBatisUtils.closeSqlSession(sqlSession);
        }
    }
    @Test
    public void testSelectPage() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtils.openSession();
            PageHelper.startPage(3, 10);
            Page<Teacher> page = (Page) sqlSession.selectList("teacher.selectPage");
            System.out.println("当前页数：" + page.getPageNum());
            System.out.println("总条数：" + page.getTotal());
            System.out.println("总页数：" + page.getPages());
            System.out.println("开始页数：" + page.getStartRow());
            System.out.println("结束页数：" + page.getEndRow());
            List<Teacher> teachers = page.getResult();
            for (Teacher teacher : teachers) {
                String name = teacher.getName();
                Integer no = teacher.getjNo();
                String description = teacher.getDescription();
                System.out.println("no = " + no);
                System.out.println("name = " + name);
                System.out.println("description = " + description);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MyBatisUtils.closeSqlSession(sqlSession);
        }
    }

}
