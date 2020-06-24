/*
 * Copyright (c) 2007 Naoki Mori
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package operator;

import java.util.Arrays;

import population.Population;
import fitness.FitnessManager;

/**
 * 個体群の適応度を評価するだけの演算子．
 * @author mori
 * @version 1.0
 */
public class EvaluateFitness implements IOperator {
	
	/**
	 * 可変長引数を利用した配列で初期化． パラメータなし．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		//パラメータ無し
    	if(params==null){
    		return;
    	}		
		try {
			//長さ0の配列は認める．
    		if(params.length!=0){    		    		
    			throw new Exception();
    		}
		}catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}
	
	/**
	 * 個体群の適応度を評価する. 個体群に変更はないが，適応度評価回数や最大適応度個体は更新される．
	 * @param pop 個体群
	 */
	public void apply(Population pop) {
		FitnessManager.getFitnessArray(pop.getIndivList());		
	}

	/**
	 * 名前を返す. 
	 * @return 名前
	 */
	public String getName() {
		return "EvaluateFitness";
	}
    /**
	 * パラメータの情報を返す．
	 * パラメータがないので空文字列を返す.   
	 * @return パラメータの情報文字列
	 */
    public String getParameterInfo(){
    	return "";
    }
    
    /**
     * 文字列化
     * @return 文字列表現
     */    
    public String toString(){
    	return getName()+"{"+getParameterInfo()+"}";
    }    
}
