package com.json;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 *  阿里巴巴的 开源类型  在1.6之前 速度较快,但也舍弃了大部分功能 或不兼容
 * 	1.对于JSON对象与JSON格式字符串的转换可以直接用 toJSONString()这个方法。
	2.javaBean与JSON格式字符串之间的转换要用到：JSON.toJSONString(obj);
	3.javaBean与json对象间的转换使用：JSON.toJSON(obj)，然后使用强制类型转换，JSONObject或者JSONArray。
 * @author SuperWang
 * @version 创建时间：2018年5月31日 上午11:45:40
 */
public class UseFastJson {
	//json字符串-简单对象型
	private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
	//json字符串-数组类型
	private static final String JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";
	//复杂格式json字符串
	private static final String COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

	/**
	 * json字符串-简单对象型与JSONObject之间的转换
	 */
	public static void testJSONStrToJSONObject() {

		JSONObject jsonObject = JSON.parseObject(JSON_OBJ_STR);
		//JSONObject jsonObject1 = JSONObject.parseObject(JSON_OBJ_STR); //因为JSONObject继承了JSON，所以这样也是可以的

		System.out.println(jsonObject.getString("studentName") + ":" + jsonObject.getInteger("studentAge"));

	}
	
	/**
     * json字符串-数组类型与JSONArray之间的转换
     */
    public static void testJSONStrToJSONArray(){

        JSONArray jsonArray = JSON.parseArray(JSON_ARRAY_STR);
        //JSONArray jsonArray1 = JSONArray.parseArray(JSON_ARRAY_STR);//因为JSONArray继承了JSON，所以这样也是可以的

        //遍历方式1
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));
        }

        //遍历方式2
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));
        }
    }
    
    /**
     * 复杂json格式字符串与JSONObject之间的转换
     */
    public static void testComplexJSONStrToJSONObject(){

        JSONObject jsonObject = JSON.parseObject(COMPLEX_JSON_STR);
        //JSONObject jsonObject1 = JSONObject.parseObject(COMPLEX_JSON_STR);//因为JSONObject继承了JSON，所以这样也是可以的
        
        String teacherName = jsonObject.getString("teacherName");
        Integer teacherAge = jsonObject.getInteger("teacherAge");
        JSONObject course = jsonObject.getJSONObject("course");
        JSONArray students = jsonObject.getJSONArray("students");

    }
    
    /**
     * json字符串与javaBean之间的转换推荐使用 TypeReference<T> 这个类，使用泛型可以更加清晰，当然也有其它的转换方式，这里就不做探讨了。
     */
    /**
     * json字符串-简单对象与JavaBean_obj之间的转换
     */
    public static void testJSONStrToJavaBeanObj(){

        Student student = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {});
        //Student student1 = JSONObject.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {});//因为JSONObject继承了JSON，所以这样也是可以的

        System.out.println(student.getStudentName()+":"+student.getStudentAge());

    }
    /**
     * json字符串-数组类型与JavaBean_List之间的转换
     */
    public static void testJSONStrToJavaBeanList(){
        
        ArrayList<Student> students = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {});
        //ArrayList<Student> students1 = JSONArray.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {});//因为JSONArray继承了JSON，所以这样也是可以的
        
        for (Student student : students) {
            System.out.println(student.getStudentName()+":"+student.getStudentAge());
        }
    }
    /**
     * 复杂json格式字符串与JavaBean_obj之间的转换
     */
    public static void testComplexJSONStrToJavaBean(){

        Teacher teacher = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});
        //Teacher teacher1 = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});//因为JSONObject继承了JSON，所以这样也是可以的
        String teacherName = teacher.getTeacherName();
        Integer teacherAge = teacher.getTeacherAge();
        Course course = teacher.getCourse();
        List<Student> students = teacher.getStudents();
    }
    /**
     * 实体类
     */
    class Student {

        private String studentName;
        private Integer studentAge;

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public Integer getStudentAge() {
            return studentAge;
        }

        public void setStudentAge(Integer studentAge) {
            this.studentAge = studentAge;
        }
    }
    class Course {

        private String courseName;
        private Integer code;

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
    class Teacher {

        private String teacherName;
        private Integer teacherAge;
        private Course course;
        private List<Student> students;

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public Integer getTeacherAge() {
            return teacherAge;
        }

        public void setTeacherAge(Integer teacherAge) {
            this.teacherAge = teacherAge;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }
}
