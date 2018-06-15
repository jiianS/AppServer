package com.gudi.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gudi.util.HttpUtil;
import com.gudi.util.PathUtil;

import net.sf.json.JSONArray;

@Controller
public class BoardController {

	@RequestMapping("/")
	public String main(HttpServletRequest req, Model m) {
		// req.getAttribute("data");
		// System.out.println("main _ " + req.getAttribute("data"));
		return "main";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/user")
	public String user() {
		return "user";
	}

	@Resource(name = "sqlSession")
	SqlSession session;

	@RequestMapping("/userInsert")
	public String userInsert(HttpServletRequest req) {
		String userEmail = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		String userName = req.getParameter("userName");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);
		param.put("userName", userName);

		// insert 되었는지 확인하기 위한 용
		int status = session.insert("user.userInsert", param);
		System.out.println(status);

		return "redirect:/";
	}

	@RequestMapping("/userSelect")
	public String userSelect(HttpServletRequest req, Model m, RedirectAttributes attr) {

		String userEmail = req.getParameter("userEmail");
		String userPassword = req.getParameter("userPassword");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userEmail", userEmail);
		param.put("userPassword", userPassword);

		HashMap<String, Object> resultMap = session.selectOne("user.userSelect", param);

		// "data"라는 key값으로 해당 값을 보내고 받기
		// redirect할때는 model을 이용하지 않고, addFlashAttribute -> 객체도 같이 보낸다
		if (resultMap == null) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("status", PathUtil.NO);
			// attr.addFlashAttribute("data", );
		} else {
			resultMap.put("status", PathUtil.OK);

		}
		attr.addFlashAttribute("data", resultMap);
		return "redirect:/";
	}

	@RequestMapping("/bInsert")
	public String bInsert() {
		System.out.println(PathUtil.FILE_DNS);
		return "board/insert";
	}

	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		String boardTitle = req.getParameter("boardTitle");
		String boardContent = req.getParameter("boardContents");
		String data = req.getParameter("data");

		System.out.println(boardTitle + ", " + boardContent);
		System.out.println(req.getParameter("data"));

		/********************************************/
		// param -> map에 담기

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContent);
		params.put("userNo", 1);

		int status = session.insert("board.boardInsert", params);
		/********************************************/

		if (status == 1) {
			int boardNo = session.selectOne("board.getBoardNo");
			System.out.println("boardNo : " + boardNo);

			List<Map<String, String>> dataList = JSONArray.fromObject(data);
			System.out.println(dataList.size());

			for (int i = 0; i < dataList.size(); i++) {
				String fileName = dataList.get(i).get("fileName").toString();
				String fileURL = dataList.get(i).get("fileURL").toString();
				System.out.println(fileName + " , " + fileURL);

				Map<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
				fileMap.put("userNo", 1);

				session.insert("board.fileInsert", fileMap);
			}

			// file_ insert
			if (status == 1) {
				map.put("msg", "글 작성이 완료 되었습니다");
				map.put("status", PathUtil.OK);
				map.put("boardNo", boardNo);
			} else {
				map.put("msg", "첨부 파일 업로드 오류 발생");
			}

		} else {
			map.put("msg", "글 작성시 오류가 발생했습니다");
		}
		return HttpUtil.makeJsonView(map);
	}

	@RequestMapping("/bSelect")
	public String bSelect() {
		System.out.println(PathUtil.FILE_DNS);
		return "board/detail";
	}

	@RequestMapping("/bld")
	public ModelAndView bld(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		Map<String, String> param = new HashMap<String, String>();
		param.put("boardNo", boardNo);
		
		HashMap<String, Object>resultMap =new HashMap<String, Object>(); 
		
		resultMap.put("boardData", session.selectOne("board.boardOne",param));
		resultMap.put("filesData", session.selectList("board.filesList",param));
		return HttpUtil.makeJsonView(resultMap);
	}

	@RequestMapping("/bList")
	public String bList() {
		return "board/list";
	}
	
	@RequestMapping("/bbld")
	public ModelAndView bbld(){		
		List list = session.selectList("board.boardList");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		
		return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/bUpdate")
	public String bUpdate() {
		return "board/update";
	}
	
	@RequestMapping("/bud")
	public ModelAndView bud(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		int boardNo = Integer.parseInt(req.getParameter("boardNo"));//parameter-> string 값으로 받아와 int로 형변환
		String boardTitle = req.getParameter("boardTitle");
		String boardContent = req.getParameter("boardContents");
		String data = req.getParameter("data");
		String delData = req.getParameter("delData");

		System.out.println(boardTitle + ", " + boardContent);
		System.out.println(req.getParameter("data"));
		System.out.println(req.getParameter("delData"));
		

		/********************************************/
		// param -> map에 담기
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("boardNo", boardNo);
		params.put("boardTitle", boardTitle);
		params.put("boardContents", boardContent);
		
		int status = session.update("board.boardUpdate", params);
		/********************************************/

		if (status == 1) {			
			List<Map<String, String>> delList = JSONArray.fromObject(delData);
			
			for(int i = 0; i < delList.size(); i++) {
				session.update("board.filesDel",delList.get(i));
			}
			
			
			List<Map<String, String>> dataList = JSONArray.fromObject(data);		

			for (int i = 0; i < dataList.size(); i++) {
				String fileName = dataList.get(i).get("fileName").toString();
				String fileURL = dataList.get(i).get("fileURL").toString();
				System.out.println(fileName + " , " + fileURL);

				Map<String, Object> fileMap = new HashMap<String, Object>();
				fileMap.put("boardNo", boardNo);
				fileMap.put("fileName", fileName);
				fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
				fileMap.put("userNo", 1);

				session.insert("board.fileInsert", fileMap);
			}

			if (status == 1) {
				map.put("msg", "글 수정이 완료 되었습니다");
				map.put("status", PathUtil.OK);
				map.put("boardNo", boardNo);
			} else {
				map.put("msg", "첨부 파일 업로드 오류 발생");
			}

		} else {
			map.put("msg", "글 작성시 오류가 발생했습니다");
		}
		return HttpUtil.makeJsonView(map);
	}

	
	
	
}
