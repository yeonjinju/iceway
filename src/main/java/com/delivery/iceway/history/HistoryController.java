package com.delivery.iceway.history;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.delivery.iceway.domain.History;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

	private final HistoryService historyService;

	/**
	 * 'history' 뷰를 위한 데이터를 모델에 추가하고 뷰 이름을 반환한다.
	 *
	 * @param model     뷰에 전달될 데이터를 담을 Model 객체.
	 * @param pageNum   현재 페이지 번호 (기본값은 1).
	 * @param keyword   사용자가 입력한 검색 키워드 (선택 사항).
	 * @param condition 사용자가 선택한 검색 조건 (선택 사항).
	 * @param dto       검색 조건을 담고 있는 History 객체.
	 * @return 뷰 이름 ('history').
	 */
	@GetMapping
	public String getHistory(
			Model model,
			@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String condition,
			History dto) {
		model.addAttribute("list", historyService.getHistory(pageNum, keyword, condition, dto));
		model.addAttribute("intArray", historyService.getPagination(pageNum, dto));
		model.addAttribute("strArray", historyService.getKeyword(dto, keyword, condition));
		model.addAttribute("recallArray" , historyService.recallArray(dto));
		return "history";
	}
}