package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Many pods on the Wolfram|Alpha website have text buttons in their upper-right corners that substitute the contents of
 * that pod with a modified version (a different state). Clicking any of these buttons will recompute just that one pod
 * to display different information. The API returns information about these pod states and allows you to
 * programmatically invoke them (similar to applying assumptions
 * <p>
 * The <states> element contains all the alternative states available for that pod. The name of each state is the same
 * as the text that appears on the Wolfram|Alpha website. You can perform the "pi" query and ask that the "More digits"
 * state be invoked automatically by using the podstate parameter, passing the value of the input attribute from the
 * corresponding <state> element:
 * <pre>
 *    http://api.wolframalpha.com/v2/query?appid=DEMO&input=pi&podstate=DecimalApproximation__More+digits
 * </pre>
 */
@Data
@XmlType(name = "states")
@XmlAccessorType(XmlAccessType.NONE)
public class States {
    @XmlAttribute(name = "count")
    private Integer count;
    @XmlElement(name = "state")
    private List<State> states;
    /**
     * Some states are logically grouped into sets, which are represented on the website as popup menus. For example,
     * the "Weather history and forecast" pod from the query "weather" shows some charts along with a popup menu that
     * controls the time period (it has values like "Current week", "Last month", etc.) This type of state control is
     * represented with the 'statelist' element. Here is the 'states' element in the API result for that pod. Note that
     * this pod has two other button-type states ("Show metric" and "More")
     * <p>
     * <pre>
     * {@code
     *
     * <states count="3">
     *      <statelist count="9" value="Current week" delimiters="">
     *          <state name="Current week" input="WeatherCharts:WeatherData__Current week"/>
     *          <state name="Current day" input="WeatherCharts:WeatherData__Current day"/>
     *          <state name="Next week" input="WeatherCharts:WeatherData__Next week"/>
     *          <state name="Past week" input="WeatherCharts:WeatherData__Past week"/>
     *          <state name="Past month" input="WeatherCharts:WeatherData__Past month"/>
     *          <state name="Past year" input="WeatherCharts:WeatherData__Past year"/>
     *          <state name="Past 5 years" input="WeatherCharts:WeatherData__Past 5 years"/>
     *          <state name="Past 10 years" input="WeatherCharts:WeatherData__Past 10 years"/>
     *          <state name="All" input="WeatherCharts:WeatherData__All"/>
     *      </statelist>
     *      <state name="Show metric" input="WeatherCharts:WeatherData__Show metric"/>
     *      <state name="More" input="WeatherCharts:WeatherData__More"/>
     *  </states>
     * }
     * </pre>
     */
    @XmlElement(name = "statelist")
    private StateList stateList;

    @Data
    @XmlType(name = "state")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class State {
        @XmlAttribute(name = "name")
        private String name;
        @XmlAttribute(name = "input")
        private String input;
    }

    /**
     * Some states are logically grouped into sets, which are represented on the website as popup menus. For example,
     * the "Weather history and forecast" pod from the query "weather" shows some charts along with a popup menu that
     * controls the time period (it has values like "Current week", "Last month", etc.) This type of state control is
     * represented with the 'statelist' element. Here is the 'states' element in the API result for that pod. Note that
     * this pod has two other button-type states ("Show metric" and "More")
     * <p>
     * <pre>
     * {@code
     *
     * <states count="3">
     *      <statelist count="9" value="Current week" delimiters="">
     *          <state name="Current week" input="WeatherCharts:WeatherData__Current week"/>
     *          <state name="Current day" input="WeatherCharts:WeatherData__Current day"/>
     *          <state name="Next week" input="WeatherCharts:WeatherData__Next week"/>
     *          <state name="Past week" input="WeatherCharts:WeatherData__Past week"/>
     *          <state name="Past month" input="WeatherCharts:WeatherData__Past month"/>
     *          <state name="Past year" input="WeatherCharts:WeatherData__Past year"/>
     *          <state name="Past 5 years" input="WeatherCharts:WeatherData__Past 5 years"/>
     *          <state name="Past 10 years" input="WeatherCharts:WeatherData__Past 10 years"/>
     *          <state name="All" input="WeatherCharts:WeatherData__All"/>
     *      </statelist>
     *      <state name="Show metric" input="WeatherCharts:WeatherData__Show metric"/>
     *      <state name="More" input="WeatherCharts:WeatherData__More"/>
     *  </states>
     * }
     * </pre>
     */
    @Data
    @XmlType(name = "stateList")
    @XmlAccessorType(XmlAccessType.NONE)
    public static class StateList {
        @XmlAttribute(name = "count")
        private Integer count;
        @XmlAttribute(name = "value")
        private String value;
        @XmlAttribute
        private String delimiters = "";
        @XmlElement(name = "state")
        private List<State> states;
    }
}
