package com.example.survey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.survey.entity.Reply;
import com.example.survey.repository.ReplyDao;
import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.SurveyUseForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class SurveyServiceTest {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private SurveyDao surveyDao;
	
	@Autowired
	private ReplyDao replyDao;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void createAc() {
		surveyService.signUp("test001over", "12345678@");
		surveyService.signUp("test002over", "12345678@");
		surveyService.signUp("test003over", "12345678@");
	}
	
	@Test
	public void createForPresent() {
		String author1 = "test001over";
		String author2 = "test002over";
		String author3 = "test003over";
		String name1 = "�ڬO�ݨ�";
		String name2 = "�ڤ��O�ݨ�";
		String description = "�o�O�ݨ��W�٪��y�z";
		boolean published = true;
		boolean unPublished = false;
		Question q1 = new Question("�ڬO��", "m", true, "A;B;C;D");
		Question q2 = new Question("�A�O��", "s", true, "A;B;C;D");
		Question q3 = new Question("�L�O��", "text", true, "");
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));
//		surveyService.create(author1, name2, description, published, LocalDate.now().plusDays(17), LocalDate.now().plusDays(23), questionList);
//		surveyService.create(author2, name1, description, unPublished, LocalDate.now().plusDays(9), LocalDate.now().plusDays(14), questionList);
//		surveyService.create(author1, name2, description, published, LocalDate.now().plusDays(7), LocalDate.now().plusDays(16), questionList);
//		surveyService.create(author3, name1, description, published, LocalDate.now().plusDays(11), LocalDate.now().plusDays(19), questionList);
//		surveyService.create(author1, name1, description, unPublished, LocalDate.now().plusDays(22), LocalDate.now().plusDays(22), questionList);
//		surveyService.create(author3, name1, description, published, LocalDate.now().plusDays(20), LocalDate.now().plusDays(23), questionList);
//		surveyService.create(author3, name2, description, published, LocalDate.now().plusDays(12), LocalDate.now().plusDays(21), questionList);
//		surveyService.create(author1, name1, description, published, LocalDate.now().plusDays(14), LocalDate.now().plusDays(20), questionList);
//		surveyService.create(author2, name2, description, unPublished, LocalDate.now().plusDays(12), LocalDate.now().plusDays(18), questionList);
//		surveyService.create(author1, name2, description, published, LocalDate.now(), LocalDate.now(), questionList);
//		surveyService.create(author3, name1, description, unPublished, LocalDate.now().plusDays(10), LocalDate.now().plusDays(22), questionList);
//		surveyService.create(author3, name1, description, published, LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), questionList);
//		surveyService.create(author2, name2, description, published, LocalDate.now(), LocalDate.now().plusDays(7), questionList);
//		surveyService.create(author1, name1, description, published, LocalDate.now(), LocalDate.now().plusDays(31), questionList);
//		surveyService.create(author3, name1, description, unPublished, LocalDate.now(), LocalDate.now().plusDays(11), questionList);
//		surveyService.create(author2, name2, description, published, LocalDate.now(), LocalDate.now(), questionList);
//		surveyService.create(author1, name2, description, published, LocalDate.now().plusDays(13), LocalDate.now().plusDays(23), questionList);
//		surveyService.create(author2, name2, description, unPublished, LocalDate.now().plusDays(11), LocalDate.now().plusDays(21), questionList);
//		surveyService.create(author1, name1, description, unPublished, LocalDate.now().plusDays(12), LocalDate.now().plusDays(24), questionList);
//		surveyService.create(author2, name2, description, unPublished, LocalDate.now(), LocalDate.now(), questionList);
		
	}
	
	@Test
	public void createTest() {
		/*********** ���T�ƭ� ***********/
		String author = "test001";
		String name = "�ڬO�ݨ�";
		String description = "�o�O�ݨ��W�٪��y�z";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("�ڬO��", "text", true, "A;B;C;D");
		Question q2 = new Question("�A�O��", "text", true, "A;B;C;D");
		Question q3 = new Question("�L�O��", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));

		/*********** ���� null ***********/
//		BaseRes res = surveyService.create(null, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null author");
//		res = surveyService.create(author, null, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null name");
//		res = surveyService.create(author, name, description, published, null, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null startDate");
//		res = surveyService.create(author, name, description, published, startDate, null, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null endDate");
//		res = surveyService.create(author, name, description, published, startDate, endDate, null);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null questionList");

		/*********** ���� logic ***********/
//		res = surveyService.create("123", name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic author");
//		res = surveyService.create(author, name, description, published, LocalDate.now().plusDays(7), LocalDate.now(),
//				questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: logic startDate&endDate");
//		questionList.add(q4);
//		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Create Test Fail: null questionList");
//		questionList.remove(3);

		/*********** ���� Success ***************/
//		res = surveyService.create(author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 200, "Create Test Fail: Success");

		// delete
	}

	@Test
	public void updateTest() {
		/*********** ���ռƭ� ***********/
		String author = "test001";
		String name = "�ڬO�ݨ�";
		String description = "�o�O�ݨ��W�٪��y�z";
		boolean published = true;
		LocalDate startDate = LocalDate.now().plusDays(2);
		LocalDate endDate = LocalDate.now().plusDays(9);
		Question q1 = new Question("�ڬO��", "text", true, "A;B;C;D");
		Question q2 = new Question("�A�O��", "text", true, "A;B;C;D");
		Question q3 = new Question("�L�O��", "text", true, "A;B;C;D");
		Question q4 = new Question();
		List<Question> questionList = new ArrayList<>(List.of(q1, q2, q3));
		// �|���o��
//		surveyService.create(author, name, description, false, startDate, endDate, questionList);
		// �|���}�l
//		surveyService.create(author, name, description, published, startDate, endDate, questionList);
		// �w�}�l
////		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
//				LocalDate.now().plusDays(3), questionList);
//		// �w����
////		surveyService.create(author, name, description, published, LocalDate.now().plusDays(-5),
//				LocalDate.now().plusDays(-2), questionList);

		/*********** ���� logic ***********/
//		BaseRes res = surveyService.update(2, author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: �|���o��");
//		res = surveyService.update(3, author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: �|���}�l");
//		res = surveyService.update(4, author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: �w�}�l");
//		res = surveyService.update(5, author, name, description, published, startDate, endDate, questionList);
//		Assert.isTrue(res.getRtnCode().getCode() == 400, "Update Test Fail: �w����");

		// delete
	}

	@Test
	public void deleteTest() {
		/*********** ���� null ***********/
		BaseRes res = surveyService.deleteSurvey(null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Delete Test Fail: null");
		/*********** ���� logic ***********/
		res = surveyService.deleteSurvey(List.of(1, 2));
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Update Test Fail: logic");
	}

	@Test
	public void replyTest() throws JsonProcessingException {
		/*********** ���T�ƭ� ***********/
		int surveyNo = 1;
		String phone = "0992345678";
		String name = "AAA";
		String email = "no@thankyou.com";
		int age = 17;
		Map<Integer, String> ans = new HashMap<>();
		ans.put(1, "B");
		ans.put(2, "CD");
		ans.put(3, "Hello");
		String answers = mapper.writeValueAsString(ans);

		/*********** ���� null ***********/
		BaseRes res = surveyService.reply(surveyNo, null, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone null");
		res = surveyService.reply(surveyNo, phone, null, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: name null");
		res = surveyService.reply(surveyNo, phone, name, null, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email null");
		res = surveyService.reply(surveyNo, phone, name, email, age, null);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: answers null");

		/*********** ���� logic ***********/
		res = surveyService.reply(-1, phone, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: surveyNo logic");
		res = surveyService.reply(surveyNo, "091234", name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: phone logic");
		res = surveyService.reply(surveyNo, phone, name, "123456", age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: email logic");
		res = surveyService.reply(surveyNo, phone, name, email, -1, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 400, "Reply Test Fail: age logic");

		/*********** ���� Success ***************/
		res = surveyService.reply(surveyNo, phone, name, email, age, answers);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Reply Test Fail: Success");
	}

	@Test
	public void searchTest() {
		SearchRes y = surveyService.search("1", null, null, true, "test01");
		System.out.println(y.toString());
		y = surveyService.search("", null, null, false, "test01");
		for(SurveyUseForm item : y.getSurveyList()) {
			System.out.println(item.getNo()+" "+item.getAuthor()+" "+item.getName());
		}

	}
	
	@Test
	public void mapTest() throws JsonProcessingException {
		Map<Integer, String> answers = new HashMap<>();
		answers.put(1, "B");
		answers.put(2, "CD");
		answers.put(3, "Hello");
		System.out.println(mapper.writeValueAsString(answers));
		String ansStr = mapper.writeValueAsString(answers);
		Map<Integer, String> ansMap = mapper.readValue(ansStr, new TypeReference<Map<Integer, String>>() {
		});
		System.out.println(ansMap.get(1));
		System.out.println(ansMap.get(2));
		System.out.println(ansMap.get(3));
	}
	
	@Test
	public void arrSortTest() throws JsonProcessingException {
		System.out.println(mapper.writeValueAsString(surveyService.statistics(43)));
	}

}
