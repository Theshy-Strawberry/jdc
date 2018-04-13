package model.ext;

import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/16.
 */
public class TreeModel {

    public Integer id;

    public String title;

    public String menu;

    public String root;

    public String parent;

    public List nodes;

    public List bakNodes;

    public String tabCode;

    public Integer authId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }

    public List getBakNodes() {
        return bakNodes;
    }

    public void setBakNodes(List bakNodes) {
        this.bakNodes = bakNodes;
    }

    public String getTabCode() {
        return this.tabCode;
    }

    public void setTabCode(String tabCode) {
        this.tabCode = tabCode;
    }

    public Integer getAuthId() {
        return this.authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }
}
