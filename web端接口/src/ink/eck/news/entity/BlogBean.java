package ink.eck.news.entity;
public class BlogBean
{
  private int id;
  private String title;
  private String description;
  private String source;
  private String picUrl;
  private String url;
  private String ctime;

  public BlogBean(){
		super();
		// TODO Auto-generated constructor stub
  }

  public BlogBean(int id, String ctime, String title, String description, String source, String picUrl, String url)
  {
    this.id = id;
    this.ctime = ctime;
    this.title = title;
    this.description = description;
    this.source = source;
    this.picUrl = picUrl;
    this.url = url;
  }

  public String getDescription()
  {
    return this.description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTitle() {
    return this.title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getSource() {
    return this.source;
  }
  public void setSource(String source) {
    this.source = source;
  }
  public String getPicUrl() {
    return this.picUrl;
  }
  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }
  public String getUrl() {
    return this.url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getCtime() {
    return this.ctime;
  }
  public void setCtime(String ctime) {
    this.ctime = ctime;
  }
}