package com.github.nginate.wolframalpha.model;

import com.github.nginate.wolframalpha.model.adapter.CDATAAdapter;
import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlType(name = "cell")
@XmlAccessorType(XmlAccessType.NONE)
public class Cell {
    @XmlAttribute
    private Boolean compressed;
    @XmlValue
    @XmlJavaTypeAdapter(CDATAAdapter.class)
    private String data;
}
