package com.github.nginate.wolframalpha.model;

/**
 * For API types that return full Wolfram|Alpha output, the layout parameter defines how content is presented. The
 * default setting is divider (shown in previous queries), which specifies a series of pods with horizontal dividers.
 * The other option, labelbar, specifies a series of separate content sections with label bar headings
 */
public enum Layout {
    DIVIDER, LABELBAR
}
