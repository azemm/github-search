package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zem on 29/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private long id;
    private String login;
    private String avatar_url;
    private String url;
    private String html_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o instanceof User) {
            return this.id == ((User)o).getId();
        }

        return false;
    }

    @Override
    public int hashCode(){
        return login.hashCode();
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
}
