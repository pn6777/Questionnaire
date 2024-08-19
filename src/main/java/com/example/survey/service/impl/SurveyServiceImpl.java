package com.example.survey.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.survey.constants.RtnCode;
import com.example.survey.entity.Login;
import com.example.survey.entity.Reply;
import com.example.survey.entity.ReplyId;
import com.example.survey.entity.Survey;
import com.example.survey.repository.LoginDao;
import com.example.survey.repository.ReplyDao;
import com.example.survey.repository.SurveyDao;
import com.example.survey.service.ifs.SurveyService;
import com.example.survey.vo.BaseRes;
import com.example.survey.vo.Question;
import com.example.survey.vo.QuestionCreate;
import com.example.survey.vo.ResultRes;
import com.example.survey.vo.SearchRes;
import com.example.survey.vo.StatisticsRes;
import com.example.survey.vo.SurveyUseForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	private SurveyDao surveyDao;

	@Autowired
	private ReplyDao replyDao;

	@Autowired
	private LoginDao loginDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * �\��G�s�W�ǤJ�����<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 * (2)�T�{��椣�s�b
	 */
	@Override
	public BaseRes create(String author, String name, String description, boolean published, LocalDate startDate,
			LocalDate endDate, List<QuestionCreate> questions) {
		if (!checkInputSurvey(author, name, startDate, endDate, questions)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		try {
			surveyDao.save(new Survey(author, name, description, published, startDate, endDate,
					mapper.writeValueAsString(questions)));
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * �\��G��s�ǤJ�����<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 * (2)�T�{���s�b�B�i�s��<br>
	 */
	@Override
	public BaseRes update(int no, String author, String name, String description, boolean published,
			LocalDate startDate, LocalDate endDate, List<QuestionCreate> questions) {
		if (!checkInputSurvey(author, name, startDate, endDate, questions)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		try {
			if (surveyDao.update(no, name, description, published, startDate, endDate,
					mapper.writeValueAsString(questions)) == 0) {
				return new BaseRes(RtnCode.SURVEY_NOT_EXISTS);
			}
		} catch (JsonProcessingException e) {
			return new BaseRes(RtnCode.JSON_ERROR);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * �\��G�R���ǤJ�����s��<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 */
	@Override
	public BaseRes deleteSurvey(List<Integer> noList) {
		if (CollectionUtils.isEmpty(noList)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		surveyDao.deleteAllByNoInAndPublishedFalseOrNoInAndStartDateAfter(noList, noList, LocalDate.now());
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * �\��G�ھڻݭn���ݨ��s���A�^�ǲέp���G
	 */
	@Override
	public StatisticsRes statistics(int no) {
		if (no < 1) {
			return new StatisticsRes(RtnCode.PARAM_ERROR, null);
		}
		// ��������s��������
		List<Reply> ans = replyDao.findBySurveyNo(no);
		if (CollectionUtils.isEmpty(ans)) {
			return new StatisticsRes(RtnCode.NO_REPLY, null);
		}
		// �P�_�����D�ػݭn�έp�A�ñN�n�έp���D�ظˤJ�}�C��
		Optional<Survey> quiz = surveyDao.findById(no);
		List<QuestionCreate> qu = new ArrayList<>();   // �̭��˵��D�ت����D�M��A�ﶵ�榡�� String�A�ݦA��";"���}
		try {
			qu = mapper.readValue(quiz.get().getQuestions(), new TypeReference<List<QuestionCreate>>() {
			});
		} catch (JsonProcessingException e) {
			return new StatisticsRes(RtnCode.JSON_ERROR, null);
		}
		List<Integer> typeOption = new ArrayList<>();     // �̭��˵��D�ت� index�A�[�@�Y�D��
		List<Integer> typeText = new ArrayList<>();     // �̭��˵��D�ت� index�A�[�@�Y�D��
		for (QuestionCreate item : qu) {
			System.out.println("136 option: "+item.getOption());
			if (StringUtils.hasText(item.getOption())) {  // �p�G���D���ﶵ�D�šA�h�ݭn�έp
				typeOption.add(qu.indexOf(item));
				System.out.println("138 add "+ qu.indexOf(item));
			} else {
				typeText.add(qu.indexOf(item));
				System.out.println("141 add "+ qu.indexOf(item));
			}
		}
		// �ھ��D���h�p��C�@�D
		for (Integer id : typeOption) {
			String ansLongString = new String("");   // �C�@�D���n�ؤ@�Ӧr��
			for (Reply eachAns : ans) {   // ���ɦ^���� String
				try {
					Map<Integer, String> eachAnsInMapForm = mapper.readValue(eachAns.getAnswers(),
							new TypeReference<Map<Integer, String>>() {   // �N�C�@���^���ഫ�� Map�A�H�D���� key
							});
					ansLongString += eachAnsInMapForm.get(id);
					countOption(id, qu.get(id).getOption(), ansLongString); // �ǤJ�D���B���D���ﶵ�B������
				} catch (JsonProcessingException e) {
					return new StatisticsRes(RtnCode.JSON_ERROR, null);
				}
			}
		}
		for(Integer id : typeText) {
			Map<String, Integer> ansCountMap = new HashMap<>();   // �C�D���n�ؤ@�� map
			for(Reply eachAns : ans) {               
				try {
					Map<Integer, String> eachAnsInMapForm = mapper.readValue(eachAns.getAnswers(),
							new TypeReference<Map<Integer, String>>() {   // �N�C�@���^���ഫ�� Map�A�H�D���� key
							});
					if(!ansCountMap.containsKey(eachAnsInMapForm.get(id))) {   // �p�G ansMap ���S���� key �ȡA��ܳo�O�Ĥ@���X�{������
						ansCountMap.put(eachAnsInMapForm.get(id), 1);
					} else {
						ansCountMap.put(eachAnsInMapForm.get(id), ansCountMap.get(eachAnsInMapForm.get(id)) + 1);
					}
				} catch (JsonProcessingException e) {
					return new StatisticsRes(RtnCode.JSON_ERROR, null);
				}
			}
			answerCountMap.put(id, ansCountMap);
		}
		return new StatisticsRes(RtnCode.SUCCESS, answerCountMap);
	}
	
	private Map<Integer, Map<String, Integer>> answerCountMap = new HashMap<>();
	
	// �ھڶǤJ���D�ؿﶵ�i��p��A�ñN�έp���G�O���b Map ��
	private void countOption(int index, String option, String ansString) {
		System.out.println("182: "+ option + " " + ansString);
		Map<String, Integer> ansCountMap = new HashMap<>(); // �C�D�i�J���|���m�@��
		String[] optionList = option.split(";");
		Arrays.sort(optionList, (a, b) -> Integer.compare(b.length(), a.length()));
		for (String op : optionList) {
			int lengthBefore = ansString.length();
			ansString = ansString.replace(op, "");
			int lengthAfter = ansString.length();
			int count = (lengthBefore - lengthAfter) / op.length();
			System.out.println("193"+" op:"+op+" lengthBefore:"+lengthBefore+" lengthAfter:"+lengthAfter+" count"+count);
			ansCountMap.put(op, count);
		}
		answerCountMap.put(index, ansCountMap);
	}

	@Override
	public ResultRes resultList(int no) {
		if (no < 1) {
			return new ResultRes(RtnCode.PARAM_ERROR, null);
		}
		return new ResultRes(RtnCode.SUCCESS, replyDao.findBySurveyNo(no));
	}

	/**
	 * �\��G�s�W�ǤJ������<br>
	 * ���b (1)�P�_��J���e�޿�<br>
	 * (2)�T�{�P�@�H�����פ��s�b
	 */
	@Override
	public BaseRes reply(int surveyNo, String name, String phone, String email, int age, String ans) {
		if (!checkInputReply(surveyNo, phone, name, email, age, ans)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		if (replyDao.existsById(new ReplyId(surveyNo, phone))) {
			return new BaseRes(RtnCode.DUPLICATED_REPLY);
		}
		replyDao.save(new Reply(surveyNo, name, phone, email, age, ans));
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * 1. �ˬd Account/Password �榡�O�_���T<br>
	 * 2. �ˬd Account �O�_�s�b<br>
	 * 3. �ˬd Password �O�_�ۦP<br>
	 **/
	@Override
	public BaseRes signIn(String account, String pw) {
		if (checkACIsF(account) || checkPWIsF(pw)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		Optional<Login> op = loginDao.findById(account);
		if (op.isEmpty()) {
			return new BaseRes(RtnCode.ACCOUNT_NOT_EXISTS);
		}
		if (!encoder.matches(pw, op.get().getPw())) {
			return new BaseRes(RtnCode.PASSWORD_INCORRECT);
		}
		return new BaseRes(RtnCode.SUCCESS);
	}

	@Override
	public BaseRes signUp(String account, String pw) {
		if (checkACIsF(account) || checkPWIsF(pw)) {
			return new BaseRes(RtnCode.PARAM_ERROR);
		}
		if (loginDao.existsById(account)) {
			return new BaseRes(RtnCode.ACCOUNT_EXISTS);
		}
		loginDao.save(new Login(account, encoder.encode(pw)));
		return new BaseRes(RtnCode.SUCCESS);
	}

	/**
	 * �\��G�P�_�O�_����x�j�M�A�j�M�ŦX�ǤJ���󪺪��
	 */
	@Override
	public SearchRes search(String surveyName, LocalDate startDate, LocalDate endDate, boolean front, String author) {
		if (!StringUtils.hasText(surveyName)) {
			surveyName = "";
		}
		if (startDate == null) {
			startDate = LocalDate.of(1912, 1, 1);
		}
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		List<Survey> temp;
		if (front) {
			temp = surveyDao.frontSearch(surveyName, startDate, endDate);
		} else {
			temp = surveyDao.backSearch(surveyName, startDate, endDate, author);
		}
		List<SurveyUseForm> resList = new ArrayList<SurveyUseForm>();
		for (Survey item : temp) {
			try {
				SurveyUseForm x = new SurveyUseForm(item.getNo(), item.getAuthor(), item.getName(),
						item.getDescription(), item.isPublished(), item.getStartDate(), item.getEndDate(), null,
						mapper.readValue(item.getQuestions(), new TypeReference<List<Question>>() {
						}));
				if (!item.isPublished()) {
					x.setState("unpublish");
				}
				if (item.getEndDate().isBefore(LocalDate.now()) && item.isPublished()) {
					x.setState("done");
				}
				if (item.getStartDate().isAfter(LocalDate.now()) && item.isPublished()) {
					x.setState("unstart");
				}
				if (!item.getStartDate().isAfter(LocalDate.now()) && !item.getEndDate().isBefore(LocalDate.now())
						&& item.isPublished()) {
					x.setState("running");
				}
				resList.add(x);
			} catch (JsonProcessingException e) {
				return new SearchRes(RtnCode.JSON_ERROR, null);
			}
		}
		return new SearchRes(RtnCode.SUCCESS, resList);
	}

	// ------------------Survey Area------------------------//
	/**
	 * 1. �T�{�ѼƬO�_�� null<br>
	 * 2. �T�{�ݨ��϶��O�_�ŦX�޿�<br>
	 * result: �榡�����T return True, �����~ return False
	 **/
	private boolean checkInputSurvey(String author, String name, LocalDate startDate, LocalDate endDate,
			List<QuestionCreate> questions) {
		if (author == null || name == null || startDate == null || endDate == null
				|| CollectionUtils.isEmpty(questions)) {
			return false;
		}
		if (!author.matches("[\\w&&[^_]]{6,14}") || startDate.isAfter(endDate)) {
			return false;
		}
		for (QuestionCreate item : questions) {
			if (item.getQuestion() == null || item.getType() == null
					|| !item.getType().equals("text") && item.getOption() == null) {
				return false;
			}
		}
		return true;
	}

	// ------------------Reply Area------------------------//
	/**
	 * 1. �T�{�ѼƬO�_null<br>
	 * 2. �T�{�϶��O�_�ŦX�޿�<br>
	 * result: �榡�����T return True, �����~ return False.
	 **/
	private boolean checkInputReply(int surveyNo, String phone, String name, String email, int age, String answers) {
		if (!StringUtils.hasText(phone) || !StringUtils.hasText(name) || !StringUtils.hasText(email)
				|| !StringUtils.hasText(answers)) {
			return false;
		}
		if (surveyNo <= 0 || !phone.matches("09\\d{8}") || !email.matches(".{1,}@.{1,}") || age < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 1. �N�ѼƱj���૬<br>
	 * 2. �N�૬�᪺ Reply ���s�J String �ഫ���� List<br>
	 * result: return ReplyUseForm
	 */
//	private ReplyUseForm convertToReplyUseForm(Reply req) throws JsonMappingException, JsonProcessingException {
//		ReplyUseForm temp = (ReplyUseForm) req;
//		temp.setAnswerList(mapper.readValue(req.getAnswers(), new TypeReference<List<Answer>>() {
//		}));
//		return temp;
//	}
	// -----------Login Area------------//
	/**
	 * 1. �ˬd Account ���O�_�����N�q��r<br>
	 * 2. �ˬd Account �榡�O�_���T<br>
	 * �Y�����T�Areturn False<br>
	 * �Y�����~�Areturn True
	 **/
	private boolean checkACIsF(String account) {
		if (!StringUtils.hasText(account)) {
			return true;
		}
		String patternAC = "[\\w&&[^_]]{6,14}";
		return !(account.matches(patternAC));
	}

	/**
	 * 1. �ˬd Password ���O�_�����N�q��r<br>
	 * 2. �ˬd Password �榡�O�_���T<br>
	 * �Y�����T�Areturn False<br>
	 * �Y�����~�Areturn True
	 **/
	private boolean checkPWIsF(String password) {
		if (!StringUtils.hasText(password)) {
			return true;
		}
		String patternPW1 = "[\\W||\\w]{8,16}";
		String patternPW2 = ".{0,}[\\W_]+.{0,}";
		return !(password.matches(patternPW1) && password.matches(patternPW2));
	}

	@Override
	public SearchRes searchForHome(int type) {
		List<Survey> temp = new ArrayList<Survey>();
		switch (type) {
		case 1:
			// �Y�N����
			temp = surveyDao.selectComingEnd();
			break;
		case 2:
			// �̪�}�l
			temp = surveyDao.selectRecentlyStart();
			break;
		case 3:
			// �Y�N�}�l
			temp = surveyDao.selectComingStart();
		}
		List<SurveyUseForm> resList = new ArrayList<SurveyUseForm>();
		for (Survey item : temp) {
			try {
				SurveyUseForm x = new SurveyUseForm(item.getNo(), item.getAuthor(), item.getName(),
						item.getDescription(), item.isPublished(), item.getStartDate(), item.getEndDate(), null,
						mapper.readValue(item.getQuestions(), new TypeReference<List<Question>>() {
						}));
				if (!item.isPublished()) {
					x.setState("unpublish");
				}
				if (item.getEndDate().isBefore(LocalDate.now()) && item.isPublished()) {
					x.setState("done");
				}
				if (item.getStartDate().isAfter(LocalDate.now()) && item.isPublished()) {
					x.setState("unstart");
				}
				if (!item.getStartDate().isAfter(LocalDate.now()) && !item.getEndDate().isBefore(LocalDate.now())
						&& item.isPublished()) {
					x.setState("running");
				}
				resList.add(x);
			} catch (JsonProcessingException e) {
				return new SearchRes(RtnCode.JSON_ERROR, null);
			}
		}
		return new SearchRes(RtnCode.SUCCESS, resList);
	}

}
