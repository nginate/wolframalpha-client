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
    /**
     * This is the text format that you see in the "Mathematica plaintext output" popup that appears when you click some
     * results on the Wolfram|Alpha site. This format is not available for all results, and it will sometimes be large (e.g.,
     * for mathematical plots), or not very useful (e.g., when the original source data is only available to Mathematica as
     * a raster image, such as a country's flag). The first formula in the "Continued fraction" pod in the Wolfram|Alpha
     * output for the query "pi" has a Mathematica output representation of {3, 7, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 2, 1, 1,
     * 2, 2, 2, 2, 1, 84, 2, 1, 1}. Use this form if you want to feed the output into Mathematica or use Mathematica as an
     * environment for manipulating results. Use the moutput format type to get results in this form.
     */
    @XmlElement
    private String moutput;
    @XmlElement
    private Cell cell;
}
