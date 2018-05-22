package com.newdumai.global.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.newdumai.system.MenuService;
@Deprecated
public class EasyuiMenuUtil {
	/**
	 * 以某个菜单为根节点，获取其所有的子节点，并将其映射成easyui的树节点
	 * 涉及到树就涉及到递归，树也是一个节点
	 * @param menu	根菜单
	 * @param node	对应于根菜单的树节点
	 * @return 以menu作为根节点的menu树(节点)
	 */
/*	public static List<EasyUITreeNode> traversal(MenuService menuService,String pid) {	
		List<EasyUITreeNode> list=new ArrayList<EasyUITreeNode>();
		EasyUITreeNode node;
		List<Map<String, Object>> menu = menuService.getChildrenById(pid);
		for (Map<String, Object> m : menu) {
			node = new EasyUITreeNode();
			node.setId(Long.valueOf((String) m.get("code_ID")));
			node.setText((String) m.get("code_NAME"));
			node.setChildren(EasyuiMenuUtil.traversal(menuService,(String) m.get("code_ID")));
			list.add(node);
		}
		return list;
		
//		//映射根节点的非children属性(不需要映射parent)
//		node.setId(rootMenu.getId());
//		node.setText(rootMenu.getName());
//		node.setIconCls(rootMenu.getIconCls());
//		Map<String, Object> attributes = new HashMap<String, Object>();
//		attributes.put("url", rootMenu.getUrl());
//		attributes.put("px", rootMenu.getPx());
//		
//		List<Map<String, Object>> children = menuService.getChildrenById(menu.getId());
//		for (MenuInfo m : children) {
//			EasyUITreeNode childNode = new EasyUITreeNode();
//			//映射非children属性
//			childNode.setId(m.getId());
//			childNode.setText(m.getName());
//			childNode.setIconCls(m.getIconCls());
//			Map<String, Object> attributes = new HashMap<String, Object>();
//			attributes.put("url", m.getUrl());
//			attributes.put("px", m.getPx());
//			attributes.put("parent", m.getParent());
//			childNode.setAttributes(attributes);
//			
//			//children属性在这里映射
//			node.getChildren().add(childNode);
//			traversal(m,childNode);
//		}
//		return node;
	}
//	private EasyUITreeNode traversal(MenuInfo menu,EasyUITreeNode node) {	
//		logger.debug("=======MenuController.traversal() invoked=======");
//		List<MenuInfo> children = menuService.getChildrenById(menu.getId());
//		for (MenuInfo m : children) {
//			EasyUITreeNode childNode = new EasyUITreeNode();
//			//映射非children属性
//			childNode.setId(m.getId());
//			childNode.setText(m.getName());
//			childNode.setIconCls(m.getIconCls());
//			Map<String, Object> attributes = new HashMap<String, Object>();
//			attributes.put("url", m.getUrl());
//			attributes.put("px", m.getPx());
//			attributes.put("parent", m.getParent());
//			childNode.setAttributes(attributes);
//			
//			//children属性在这里映射
//			node.getChildren().add(childNode);
//			traversal(m,childNode);
//		}
//		return node;
//	}
*/
}
