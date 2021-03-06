package lemon.web.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lemon.web.base.AdminNavAction;
import lemon.web.system.bean.Menu;
import lemon.web.system.bean.User;
import lemon.web.system.mapper.MenuMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统菜单管理
 * 
 * @author lemon
 * @version 1.0
 * 
 */
@Controller
@RequestMapping("/system/menu")
public final class MenuAction extends AdminNavAction {
	@Autowired
	private MenuMapper menuMapper;

	/**
	 * 显示菜单列表[无需分页]
	 * @param session
	 * @return
	 */
	@RequestMapping("list")
	public ModelAndView list(HttpSession session) {
		//获取Main视图名称
		String mainViewName = getMainViewName(Thread.currentThread().getStackTrace()[1].getMethodName());
		if(null == mainViewName)
			sendNotFoundError();
		//获取用户角色
		User user = (User) session.getAttribute(TOKEN);
		//获取导航条数据
		Map<String, Object> resultMap = buildNav(user.getRole_id());
		//获取Main数据
		List<Menu> menuList = obtainMenuTree();
		resultMap.put("mainViewName", mainViewName);
		resultMap.put("menuList", menuList);
		return new ModelAndView(VIEW_MANAGE_HOME_PAGE, "page", resultMap);
	}
	
	/**
	 * 保存菜单
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String save(@Valid Menu menu, BindingResult br) {
		Menu supMenu = menuMapper.getMenu(menu.getSupmenucode());
		if(supMenu == null)
			return sendJSONError("保存失败： 菜单信息不完整。");
		if(br.hasErrors())
			return sendJSONError(br.getFieldError().getDefaultMessage());
		String lev = String.valueOf(Integer.parseInt(supMenu.getMenulevcod())+1);
		menu.setMenulevcod(lev);
		int result = 0;
		if(menu.getMenu_id() <= 0)
			result = menuMapper.addMenu(menu);
		else
			result = menuMapper.editMenu(menu);
		if(result == 0)
			return sendJSONError("菜单保存失败。");
		else
			return sendJSONMsg("菜单保存成功。");
	}
	
	/**
	 * 删除菜单
	 * @param menu_id
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delete(String menu_id) {
		String[] ids = menu_id.split(",");
		int result = menuMapper.deleteMenu(ids);
		if (result == 0)
			return sendJSONError("菜单删除失败。");
		else
			return sendJSONMsg("菜单删除成功。");
	}
	
	/**
	 * 显示添加或者编辑菜单的页面
	 * @param menu_id
	 * @return
	 */
	@RequestMapping(value="add-edit-page")
	public ModelAndView addOrEditPage(int menu_id) {
		// 查询上级菜单
		List<Menu> pmList = menuMapper.getParentMenuList();
		// 查询当前菜单
		Menu menu = null;
		if (menu_id != 0)
			menu = menuMapper.getMenu(menu_id);
		if(menu == null)
			menu = new Menu();
		Map<String, Object> result = new HashMap<>();
		result.put("pmList", pmList);
		result.put("menu", menu);
		return new ModelAndView(getAddEditView(), "result", result);
	}

	@Override
	protected String getMenuURL() {
		return "system/menu";
	}
	
	@Override
	protected int lastPage(int currentPage, int rsCnt) {
		return 1;
	}
	
	/**
	 * 生成菜单树
	 * @return
	 */
	private List<Menu> obtainMenuTree(){
		//获取二级菜单
		List<Menu> l2_list = menuMapper.getMenuListByLevel("2");
		//三级菜单
		List<Menu> l3_list = null;
		//结果
		List<Menu> result = new ArrayList<>();
		for (Menu parent : l2_list) {
			result.add(parent);
			l3_list = menuMapper.getMenuListByParent(parent.getMenu_id());
			if(l3_list == null || l3_list.size() == 0)
				continue;
			for (Menu l3 : l3_list) {
				result.add(l3);
			}
		}
		l2_list.clear();
		l3_list.clear();
		return result;
	}

}
