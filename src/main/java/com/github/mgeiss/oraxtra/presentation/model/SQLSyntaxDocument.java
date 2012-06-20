/*
 * Copyright 2012 Markus Geiss.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mgeiss.oraxtra.presentation.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Markus Geiss
 * @version 1.0
 */
public class SQLSyntaxDocument extends DefaultStyledDocument {

    public LinkedList<String> keywords;
    private SimpleAttributeSet highlightAttributes;

    public SQLSyntaxDocument() {
        super();
        this.initKeywords();
        this.initHighlightAttributes();
    }

    public void highlighKeyword() {
        this.setCharacterAttributes(0, this.getLength(), SimpleAttributeSet.EMPTY, true);
        try {
            String text = super.getText(0, this.getLength());
            StringTokenizer tokenizer = new StringTokenizer(text, " ");

            int offset = 0;
            String testee = null;
            while (tokenizer.hasMoreTokens()) {
                testee = tokenizer.nextToken();
                if (testee.startsWith(",") || testee.endsWith(",")) {
                    testee = testee.replaceAll(",", "");
                }
                if (this.keywords.contains(testee.toLowerCase())) {
                    int newOffset = text.indexOf(testee, offset);
                    this.setCharacterAttributes(newOffset, testee.length(), this.highlightAttributes, true);
                    offset = newOffset;
                }
                offset++;
                offset += testee.length();
            }
        } catch (Exception ex) {
        }
    }

    @Override
    protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
        this.highlighKeyword();
    }

    @Override
    protected void removeUpdate(DefaultDocumentEvent chng) {
        this.highlighKeyword();
    }

    private void initKeywords() {
        this.keywords = new LinkedList<>();
        this.keywords.add("select");
        this.keywords.add("distinct");
        this.keywords.add("min");
        this.keywords.add("max");
        this.keywords.add("avg");
        this.keywords.add("from");
        this.keywords.add("join");
        this.keywords.add("on");
        this.keywords.add("left");
        this.keywords.add("right");
        this.keywords.add("where");
        this.keywords.add("in");
        this.keywords.add("like");
        this.keywords.add("between");
        this.keywords.add("as");
        this.keywords.add("and");
        this.keywords.add("group");
        this.keywords.add("order");
        this.keywords.add("by");
        this.keywords.add("escape");
        this.keywords.add("asc");
        this.keywords.add("desc");
    }

    private void initHighlightAttributes() {
        this.highlightAttributes = new SimpleAttributeSet();
        StyleConstants.setForeground(this.highlightAttributes, (Color) UIManager.getColor("TitledBorder.titleColor"));
        StyleConstants.setBold(this.highlightAttributes, true);
    }
}
