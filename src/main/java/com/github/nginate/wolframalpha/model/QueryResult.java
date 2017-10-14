package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Contains the entire API result. The <queryresult> element is a superelement of all others listed. It has the
 * following attributes
 */
@Data
@XmlType(name = "queryresult")
@XmlRootElement(name = "queryresult")
@XmlAccessorType(XmlAccessType.NONE)
public class QueryResult {
    /**
     * true or false depending on whether the input could be successfully understood. If false, there will be no <pod>
     * subelements
     */
    @XmlAttribute(required = true)
    private Boolean success;
    /**
     * true or false depending on whether a serious processing error occurred, such as a missing required parameter. If
     * true, there will be no pod content, just an <error> subelement
     */
    @XmlAttribute(required = true)
    private Boolean error;
    /**
     * The number of pods
     */
    @XmlAttribute(required = true)
    private Integer numpods;
    /**
     * The version specification of the API on the server that produced this result
     */
    @XmlAttribute(required = true)
    private String version;
    /**
     * Categories and types of data represented in the results (e.g. "Financial").
     */
    @XmlAttribute
    private List<String> datatypes;
    /**
     * The wall-clock time in seconds required to generate the output
     */
    @XmlAttribute(required = true)
    private Float timing;
    /**
     * The number of pods that are missing because they timed out.
     *
     * @see <a href="http://products.wolframalpha.com/api/documentation/#timeouts-and-asynchronous-behavior">the timeout
     * query parameters</>
     */
    @XmlAttribute(required = true)
    private List<String> timedout;
    /**
     * The time in seconds required by the parsing phase.
     */
    @XmlAttribute
    private Float parsetiming;
    /**
     * Whether the parsing stage timed out (try a longer parsetimeout parameter if true).
     */
    @XmlAttribute
    private Boolean parsetimedout;
    /**
     * Multiple scanners are at work to produce pods relating to various topics. Any scanner that does not finish within
     * the scantimeout period is interrupted before it produces a pod. If this happens, the 'queryresult' element will
     * name the scanners that timed out in its timedout attribute, and the recalculate attribute will have a non-empty
     * value giving a URL. You can call this URL to redo the query with a longer scantimeout to give the scanners that
     * timed out a chance to finish and give you some new pods. The advantage of using the recalculate URL instead of
     * simply redoing the original query yourself and specifying a longer scantimeout is that the recalculate operation
     * is much faster because it is able to skip a lot of the work that was done in the original query. For example,
     * pods that were already computed are not computed again.
     */
    @XmlAttribute
    private String recalculate;
    /**
     * <pod> elements are the main output of the Full Results API. Each pod contains a piece or category of information
     * about the given query. It has the following attributes
     */
    @XmlElement(name = "pod")
    private List<Pod> pods;
    /**
     * The <assumptions> element is a subelement of <queryresult>. Its content is a series of <assumption> elements. It
     * has a count attribute, giving the number of <assumption> subelements. See the “Assumptions” section of the main
     * text for more details.
     */
    @XmlElement
    private Assumptions assumptions;
}
