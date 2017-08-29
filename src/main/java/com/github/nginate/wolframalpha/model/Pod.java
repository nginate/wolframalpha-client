package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * <pod> elements are the main output of the Full Results API. Each pod contains a piece or category of information
 * about the given query.
 */
@Data
@XmlType(name = "pod")
@XmlAccessorType(XmlAccessType.NONE)
public class Pod {
    /**
     * The pod title, used to identify the pod and its contents
     */
    @XmlAttribute
    private String title;
    /**
     * true or false depending on whether a serious processing error occurred with this specific pod. If true, there
     * will be an <error> subelement
     */
    @XmlAttribute
    private Boolean error;
    /**
     * A number indicating the intended position of the pod in a visual display. These numbers are typically multiples
     * of 100, and they form an increasing sequence from top to bottom
     */
    @XmlAttribute
    private Integer position;
    /**
     * The name of the scanner that produced this pod. A general guide to the type of data it holds
     */
    @XmlAttribute
    private String scanner;
    /**
     * A unique identifier for a pod, used for selecting specific pods to include or exclude.
     */
    @XmlAttribute
    private String id;
    /**
     * The number of subpod elements present.
     */
    @XmlAttribute
    private Integer numsubpods;
    /**
     * Although Wolfram|Alpha returns many pods for most queries, there is sometimes the notion of a "primary result"
     * for a given query. This is especially true for queries that correspond to Wolfram Language computations (e.g.
     * "2+2") or simple data lookups (e.g. "France GDP"). If you are looking to display the closest thing to a simple
     * "answer" that Wolfram|Alpha can provide, you can look for pods tagged as primary results via the primary=true
     * attribute.
     */
    @XmlAttribute
    private Boolean primary;
    /**
     * Subelements of <pod> that contain the results for a single subpod. <subpod> has a title attribute, which is
     * usually an empty string because most subpods have no title
     */
    @XmlElement(name = "subpod")
    private List<Subpod> subpods;
    /**
     * List of sound elements included in response if was requested
     */
    @XmlElement
    private Sounds sounds;
}
