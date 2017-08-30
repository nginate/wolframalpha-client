package com.github.nginate.wolframalpha.model.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return "<![CDATA[" + v + "]]>";
    }
}
