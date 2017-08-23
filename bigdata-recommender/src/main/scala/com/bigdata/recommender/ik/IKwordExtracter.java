package com.bigdata.recommender.ik;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKwordExtracter{
	/**
	 * 切分句子，过滤非关键词
	 */
	public static List<String> extractWords(String str) {
		List<String> words = new ArrayList<String>();
		if(str != null && !str.isEmpty()) {
			StringReader sr = new StringReader(str);
			IKSegmentation ik = new IKSegmentation(sr, false);
			Lexeme lex = null;
			try {
				while ((lex = ik.next()) != null) {
					if(lex.getLexemeType()==Lexeme.TYPE_CJK_NORMAL||lex.getLexemeType()==Lexeme.TYPE_LETTER) {
						words.add(lex.getLexemeText());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return words;
	}

}
