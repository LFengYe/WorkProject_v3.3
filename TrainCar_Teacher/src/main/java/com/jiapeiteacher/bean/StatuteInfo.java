package cn.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class StatuteInfo {


    /**
     * Id : 1
     * Title : 法规标题
     */

    private int Id;
    private String Title;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    @Override
    public String toString() {
        return "StatuteInfo{" +
                "Id=" + Id +
                ", Title='" + Title + '\'' +
                '}';
    }
}
