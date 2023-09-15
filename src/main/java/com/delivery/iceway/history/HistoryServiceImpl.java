package com.delivery.iceway.history;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

import com.delivery.iceway.domain.History;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
	private final HistoryMapper historyMapper;
	/**
	 * 특정 조건에 따른 History 목록을 페이지네이션하여 조회한다.
	 *
	 * @param pageNum   페이지 번호. 1부터 시작하며 이에 따라 시작 행과 끝 행이 계산된다.
	 * @param keyword   검색 키워드. 이 키워드는 검색 조건과 연관이 있다.
	 * @param condition 검색 조건. 예를 들어, '이름'이나 'ID' 등이 될 수 있다.
	 * @param dto       History 객체. 시작 행(startRowNum)과 끝 행(endRowNum), 그 외 검색 조건이
	 *                  저장될 객체이다.
	 * @return History 객체의 목록을 List<History> 형태로 반환한다.
	 */
	@Override
	public List<History> getHistory(int pageNum, String keyword, String condition, History dto) {
		final int PAGE_ROW_COUNT = 10;
		String strPageNum = Integer.toString(pageNum);
		if (strPageNum != null) {
			pageNum = Integer.parseInt(strPageNum);
		}
		int startRowNum = 1 + (pageNum - 1) * PAGE_ROW_COUNT;
		int endRowNum = pageNum * PAGE_ROW_COUNT;
		dto.setStartRowNum(startRowNum);
		dto.setEndRowNum(endRowNum);
		getKeyword(dto, keyword, condition);
		recallArray(dto);
		getPagination(pageNum, dto);
		return historyMapper.getHistory(dto);
	}
	/**
	 * 페이징 정보를 계산하여 반환한다.
	 *
	 * @param pageNum 현재 페이지 번호.
	 * @param dto     검색 조건을 담고 있는 History 객체.
	 * @return 페이징 정보를 담은 int 배열. 배열의 각 원소는 다음과 같다.
	 */
	@Override
	public int[] getPagination(int pageNum, History dto) {
		final int PAGE_ROW_COUNT = 10;
		final int PAGE_DISPLAY_COUNT = 5;
		int[] intArray = new int[5];
		int totalRow = historyMapper.getCount(dto);
		int startPageNum = 1 + ((pageNum - 1) / PAGE_DISPLAY_COUNT) * PAGE_DISPLAY_COUNT;
		int endPageNum = startPageNum + PAGE_DISPLAY_COUNT - 1;
		int totalPageCount = (int) Math.ceil(totalRow / (double) PAGE_ROW_COUNT);
		if (endPageNum > totalPageCount) {
			endPageNum = totalPageCount;
		}
		intArray[0] = pageNum;
		intArray[1] = startPageNum;
		intArray[2] = endPageNum;
		intArray[3] = totalPageCount;
		intArray[4] = totalRow;
		return intArray;
	}
	/**
	 * 검색 키워드와 조건을 처리하여 History 객체에 적용한다.
	 *
	 * @param dto       검색 조건을 적용할 History 객체.
	 * @param keyword   사용자가 입력한 검색 키워드.
	 * @param condition 사용자가 선택한 검색 조건.
	 * @return 키워드와 조건을 담은 문자열 배열. 배열의 원소는 다음과 같다:
	 */
	@Override
	public String[] getKeyword(History dto, String keyword, String condition) {
		if (keyword == null) {
			keyword = "";
			condition = "";
		}
		if (!keyword.equals("")) {
			if (condition.equals("name")) {
				dto.setName(keyword);
			} else if (condition.equals("delivery_time")) {
				dto.setDateString(keyword);
				SimpleDateFormat dateFormat;
				String datePattern = "";
				if (dto.getDateString().length() == 14) {
					datePattern = "yyyyMMddHHmmss";
				} else if (dto.getDateString().length() == 12) {
					datePattern = "yyyyMMddHHmm";
				} else if (dto.getDateString().length() == 10) {
					datePattern = "yyyyMMddHH";
				} else if (dto.getDateString().length() == 8) {
					datePattern = "yyyyMMdd";
				} else if (dto.getDateString().length() == 6) {
					datePattern = "yyyyMM";
				} else if (dto.getDateString().length() == 4) {
					datePattern = "yyyy";
				}
				if (!datePattern.isEmpty()) {
					dateFormat = new SimpleDateFormat(datePattern);
					try {
						Date stringToDate = dateFormat.parse(dto.getDateString());
						Timestamp stringToTimestamp = new Timestamp(stringToDate.getTime());
						dto.setDelivery_time(stringToTimestamp);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					throw new IllegalArgumentException("Invalid date format: " + dto.getDateString());
				}
			} else if (condition.equals("arrival_time")) {
				dto.setDateString(keyword);
				SimpleDateFormat dateFormat;
				String datePattern = "";
				if (dto.getDateString().length() == 14) {
					datePattern = "yyyyMMddHHmmss";
				} else if (dto.getDateString().length() == 12) {
					datePattern = "yyyyMMddHHmm";
				} else if (dto.getDateString().length() == 10) {
					datePattern = "yyyyMMddHH";
				} else if (dto.getDateString().length() == 8) {
					datePattern = "yyyyMMdd";
				} else if (dto.getDateString().length() == 6) {
					datePattern = "yyyyMM";
				} else if (dto.getDateString().length() == 4) {
					datePattern = "yyyy";
				}
				if (!datePattern.isEmpty()) {
					dateFormat = new SimpleDateFormat(datePattern);
					try {
						Date stringToDate = dateFormat.parse(dto.getDateString());
						Timestamp stringToTimestamp = new Timestamp(stringToDate.getTime());
						dto.setArrival_time(stringToTimestamp);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					throw new IllegalArgumentException("Invalid date format: " + dto.getDateString());
				}
			}
		}
		String[] pairWord = new String[2];
		pairWord[0] = keyword;
		pairWord[1] = condition;
		return pairWord;
	}
	@Override
	public String[] recallArray(History dto) {
		List<History> list = historyMapper.getHistory(dto);
		String[] recallArray = new String[list.size()];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		
		for(int i = 0; i < list.size(); i++) {
			Timestamp delivery_time = list.get(i).getRecall_time();
			Boolean recallStatus = list.get(i).isRecall_status();
			String timeString = "";
			
			if(delivery_time != null) {
				timeString = sdf.format(delivery_time);
			}
			String formattedDate = recallStatus ? "O(" + timeString + ")" : "-";
			recallArray[i] = formattedDate;
		}
		return recallArray;
	}
}