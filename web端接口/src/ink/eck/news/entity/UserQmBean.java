package ink.eck.news.entity;
public class UserQmBean
{
  private String username;
  private String qm;

  public UserQmBean(){
		super();
		// TODO Auto-generated constructor stub
  }

  public UserQmBean(String username, String qm)
  {
    this.username = username;
    this.qm = qm;
  }
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getQm() {
    return this.qm;
  }
  public void setQm(String qm) {
    this.qm = qm;
  }
}