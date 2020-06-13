package com.example.android.covid19guide.datamodels;

/**
 * An {@link Article} object contains information related to a Single News' Article.
 */
public class Article {

    /**
     * Title of the News Article
     */
    private String title;

    /**
     * Body Text of the News Article
     */
    private String description;

    /**
     * Section of the News Article
     */
    private String section;

    /**
     * Publishing Date of the News Article
     */
    private String date;

    /**
     * Website URL of the News Article
     */
    private String url;

    /**
     * Author of the News Article
     */
    private String authorName;

    /**
     * Thumbnail of the News Article
     */
    private String articleImageUrl;

    /**
     * Constructs a new {@link Article} object.
     *
     * @param title       is the title of the News Article
     * @param description is the content of the News Article
     * @param section     is the section of the News Article
     * @param date        is the date on which the News Article was published
     * @param url         is the Website URL of the News Article
     * @param authorName  is the name of the author of the News Article
     */
    public Article(String title, String description, String section, String date, String url, String authorName, String articleImageUrl) {
        this.title = title;
        this.description = description;
        this.section = section;
        this.date = date;
        this.url = url;
        this.authorName = authorName;
        this.articleImageUrl = articleImageUrl;
    }

    /**
     * This method returns the title of the News Article
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method returns the body text of the News Article
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method returns the section of the News Article
     */
    public String getSection() {
        return section;
    }

    /**
     * This method returns the date on which the News Article was published
     */
    public String getDate() {
        return date;
    }

    /**
     * This method returns the Website URL of the News Article
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method returns the name of the author of the News Article
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * This method returns the article image thumbnail url of the News Article
     */
    public String getArticleImageUrl() {
        return articleImageUrl;
    }
}
