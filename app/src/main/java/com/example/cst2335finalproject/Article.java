/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/

package com.example.cst2335finalproject.NYTArticle;

import java.io.Serializable;

public class Article implements Serializable {

    private String title;
    private String organization;
    private String articleID;

    /**
     * title body and new id constructor
     *
     * @param title
     * @param organization
     * @param articleID
     */

    public Article(String title, String organization, String articleID) {
        this.title = title;
        this.organization = organization;
        this.articleID = articleID;
    }

    /**
     * @return
     */
    public String getTitle() {

        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public String getOrganization() {

        return organization;
    }

    /**
     * @param organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return organization
     */

    public String getArticleID() {
        return articleID;
    }

    /**
     * @param articleID
     */
    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }


}






