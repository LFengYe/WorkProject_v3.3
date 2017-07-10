package cn.guugoo.jiapeiteacher.bean;

/**
 * Created by gpw on 2016/8/12.
 * --加油
 */
public class ScoreInfo {

    /**
     * HeadPortrait : 头像
     * ComprehensiveLevel : 4
     * Attitude : 3
     * Technology : 5
     * Appearance : 4
     * CarCondition : 3
     */

    private String HeadPortrait;
    private float ComprehensiveLevel;
    private float Attitude;
    private float Technology;
    private float Appearance;
    private float CarCondition;

    public String getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(String HeadPortrait) {
        this.HeadPortrait = HeadPortrait;
    }

    public float getComprehensiveLevel() {
        return ComprehensiveLevel;
    }

    public void setComprehensiveLevel(float ComprehensiveLevel) {
        this.ComprehensiveLevel = ComprehensiveLevel;
    }

    public float getAttitude() {
        return Attitude;
    }

    public void setAttitude(float Attitude) {
        this.Attitude = Attitude;
    }

    public float getTechnology() {
        return Technology;
    }

    public void setTechnology(float Technology) {
        this.Technology = Technology;
    }

    public float getAppearance() {
        return Appearance;
    }

    public void setAppearance(float Appearance) {
        this.Appearance = Appearance;
    }

    public float getCarCondition() {
        return CarCondition;
    }

    public void setCarCondition(float CarCondition) {
        this.CarCondition = CarCondition;
    }
}
