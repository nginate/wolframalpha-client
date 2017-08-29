package com.github.nginate.wolframalpha.model;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlType(name = "subpod")
@XmlAccessorType(XmlAccessType.NONE)
public class Subpod {
    /**
     * usually an empty string because most subpods have no title
     */
    @XmlAttribute
    private String title;
    @XmlAttribute
    private Boolean primary;

    @XmlElement
    private Image img;
    @XmlElement(name = "imagemap")
    private ImageMap imageMap;
    @XmlElement
    private String plaintext;
    @XmlElement(name = "mathml")
    private Object mathMl;
    /**
     * This is the text format that you see in the "Mathematica plaintext input" popup that appears when you click some
     * results on the Wolfram|Alpha site. Some results can be generated directly by single Mathematica input
     * expressions. For example, the "Continued fraction" pod in the Wolfram|Alpha result for the query "pi" has a
     * Mathematica input representation of ContinuedFraction[Pi, 25]. Use this form if you want to feed the
     * input into Mathematica or use Mathematica as an environment for manipulating results. Use the minput format
     * type to get results in this form.
     */
    @XmlElement
    private String minput;
}
