package com.gudi.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gudi.app.dao.DaoInterface;
import com.gudi.util.PathUtil;
import com.gudi.util.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class BoardController {

	@RequestMapping("/bInsert")
	public String bInsert(HttpSession session) {
		if (HttpUtil.checkLogin(session)) {
			return "board/insert";
		} else {
			return "redirect:/login";
		}
	}

	@Autowired
	DaoInterface di;

	@RequestMapping("/bid")
	public ModelAndView bid(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (HttpUtil.checkLogin(session)) {
			String boardTitle = req.getParameter("boardTitle");
			String boardContents = req.getParameter("boardContents");
			String data = req.getParameter("data");
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

			/********************************************************************************************/
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("boardTitle", boardTitle);
			params.put("boardContents", boardContents);
			params.put("userNo", userMap.get("userNo"));
			params.put("sqlType", "board.boardInsert");
			params.put("sql", "insert");
			int status = (int) di.call(params);
			/********************************************************************************************/

			if (status == 1) {
				params = new HashMap<String, Object>();
				params.put("sqlType", "board.getBoardNo");
				params.put("sql", "selectOne");
				int boardNo = (int) di.call(params);
				// int boardNo = session.selectOne("board.getBoardNo");
				List<Map<String, Object>> dataList = JSONArray.fromObject(data);

				for (int i = 0; i < dataList.size(); i++) {
					String fileName = dataList.get(i).get("fileName").toString();
					String fileURL = dataList.get(i).get("fileURL").toString();
					HashMap<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("boardNo", boardNo);
					fileMap.put("fileName", fileName);
					fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
					fileMap.put("userNo", userMap.get("userNo"));
					fileMap.put("sqlType", "board.fileInsert");
					fileMap.put("sql", "insert");

					status = (int) di.call(fileMap);
					// status = session.insert("board.fileInsert", fileMap);
				}

				if (status == 1) {
					map.put("msg", "글작성이 완료 되었습니다.");
					map.put("status", PathUtil.OK);
					map.put("boardNo", boardNo);
				} else {
					map.put("msg", "첨부파일 오류 발생.");
				}
			} else {
				map.put("msg", "글 작성 시 오류 발생.");
			}
		} else {
			map.put("msg", "로그인이 되어 있지 않습니다.");
		}

		return HttpUtil.makeJsonView(map);
	}

	@RequestMapping("/bSelect")
	public String bSelect() {
		return "board/detail";
	}

	@RequestMapping("/bld")
	public ModelAndView bld(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		param.put("sqlType", "board.boardOne");
		param.put("sql", "selectOne");
		resultMap.put("boardData", di.call(param));

		param.put("sqlType", "board.filesList");
		param.put("sql", "selectList");
		resultMap.put("filesData", di.call(param));

		return HttpUtil.makeJsonView(resultMap);
	}

	@RequestMapping("/bList")
	public String bList() {
		return "board/list";
	}

	@RequestMapping("/bbld")
	public ModelAndView bbld() {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("sqlType", "board.boardList");
		param.put("sql", "selectList");
		List list = (List) di.call(param);
		// List list = session.selectList("board.boardList");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		return HttpUtil.makeJsonView(resultMap);
	}

	@RequestMapping("/bUpdate")
	public String bUpdate(HttpSession session, HttpServletRequest req, RedirectAttributes ra) {
		if (HttpUtil.checkLogin(session)) {
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");
			HashMap<String, Object> paramMap = HttpUtil.getParamMap(req);
			String userNo1 = userMap.get("userNo").toString();
			paramMap.put("sqlType", "board.boardOne");
			paramMap.put("sql", "selectOne");
			HashMap<String, Object> resultMap = (HashMap<String, Object>) di.call(paramMap);
			String userNo2 = resultMap.get("userNo").toString();
			if (userNo1.equals(userNo2)) {
				return "board/update";
			} else {
				ra.addAttribute("boardNo", paramMap.get("boardNo"));
				return "redirect:/bSelect";
			}
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping("/bud")
	public ModelAndView bud(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (HttpUtil.checkLogin(session)) {
			int boardNo = Integer.parseInt(req.getParameter("boardNo"));
			String boardTitle = req.getParameter("boardTitle");
			String boardContents = req.getParameter("boardContents");
			String data = req.getParameter("data");
			String delData = req.getParameter("delData");
			HashMap<String, Object> userMap = (HashMap<String, Object>) session.getAttribute("user");

			/********************************************************************************************/
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("boardNo", boardNo);
			params.put("boardTitle", boardTitle);
			params.put("boardContents", boardContents);

			params.put("sqlType", "board.boardUpdate");
			params.put("sql", "update");
			int status = (int) di.call(params);
			// int status = session.update("board.boardUpdate", params);
			/********************************************************************************************/

			if (status == 1) {
				List<JSONObject> delList = JSONArray.fromObject(delData);
				for (int i = 0; i < delList.size(); i++) {
					JSONObject j = delList.get(i);
					params = new HashMap<String, Object>();
					params.put("fileNo", j.get("fileNo"));
					params.put("sqlType", "board.filesDel");
					params.put("sql", "update");
					di.call(params);
					// session.update("board.filesDel", delList.get(i));
				}

				List<Map<String, Object>> dataList = JSONArray.fromObject(data);
				for (int i = 0; i < dataList.size(); i++) {
					String fileName = dataList.get(i).get("fileName").toString();
					String fileURL = dataList.get(i).get("fileURL").toString();
					HashMap<String, Object> fileMap = new HashMap<String, Object>();
					fileMap.put("boardNo", boardNo);
					fileMap.put("fileName", fileName);
					fileMap.put("fileURL", PathUtil.FILE_DNS + fileURL);
					fileMap.put("userNo", userMap.get("userNo"));
					fileMap.put("sqlType", "board.fileInsert");
					fileMap.put("sql", "insert");
					di.call(fileMap);
					// status = session.insert("board.fileInsert", fileMap);
				}

				if (status == 1) {
					map.put("msg", "글수정이 완료 되었습니다.");
					map.put("status", PathUtil.OK);
					map.put("boardNo", boardNo);
				} else {
					map.put("msg", "첨부파일 오류 발생.");
				}
			} else {
				map.put("msg", "글 작성 시 오류 발생.");
			}
		} else {
			map.put("msg", "로그인이 되어 있지 않습니다.");
		}

		return HttpUtil.makeJsonView(map);
	}

	@RequestMapping("/bDel")
	public String bDel(HttpServletRequest req) {
		String boardNo = req.getParameter("boardNo");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("boardNo", boardNo);

		param.put("sqlType", "board.boardDel");
		param.put("sql", "update");
		int status1 = (int) di.call(param);
		System.out.println(status1);

		param.put("sqlType", "board.filesBoardDel");
		param.put("sql", "update");
		int status2 = (int) di.call(param);
		System.out.println(status2);

		return "redirect:/bList";
	}
}
