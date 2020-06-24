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

package gabuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import util.LocalTestException;

/**
 * Builderパターンの Director.
 * @author mori
 * @version 1.0
 */
public class Director {
	/**
	 * ビルダ
	 */
	private IGABuilder builder_;

	/**
	 * GAParameters への参照
	 */
	private GAParameters gaParams_;

	/**
	 * ビルダで初期化
	 * @param builder ビルダ
	 */
	public Director(IGABuilder builder) {
		builder_ = builder;
	}

	/**
	 * ビルダに各パラメータを渡す.
	 */
	public void constract() {
		builder_.setGAParameters(getGAParams());
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		// まずビルダを宣言．
		IGABuilder builder = new DefaultGABuilder();
		// ビルダを引数としてディレクタを初期化．
		Director d = new Director(builder);
		// パラメータをセット．
		d.setParameter(args);
		// 以下を実行すると builder が適切に設定される．
		d.constract();
		// ビルダの作った GA をもらう．
		GABase ga=builder.buildGA();
		try {
			ga.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * パラメータ設定用引数から GAParameters の parametersMap を作る．
	 * @param params パラメータリスト．-{記号} 値 の形で設定する．
	 */
	public void setParameter(String[] params) {
		// null の場合は何もなしと考える．
		if (params == null) {
			return;
		}
		// まず，-h か -help が合った場合には printHelp() を呼んで終了．
		if (Arrays.binarySearch(params, "-h") >= 0
				|| Arrays.binarySearch(params, "-help") >= 0) {
			printHelp();
			System.exit(-1);
		}

		try {
			// パラメータ数が一つであればファイルによる設定とみなす．
			if (params.length == 1) {
				setGAParamsByCSVFile(params[0]);
				return;
			}
			// パラメータ数が一以外で奇数であればエラー．
			if (params.length % 2 != 0) {
				throw new Exception("引数の数が偶数ではありません． -h でヘルプが表示されます．");
			}
			// params から 2 個ずつ取り出し，result に入れていく．
			for (int i = 0; i < params.length; i += 2) {
				// ハイフン付きの部分．
				String hyphenPart = params[i];
				String val = params[i + 1];
				if (hyphenPart.charAt(0) != '-') {
					printHelp();
					throw new Exception(hyphenPart
							+ " にハイフンが付いていません. -h でヘルプが表示されます．");
				}
				if (!getGAParams().getRegalOption().contains(hyphenPart)) {
					printHelp();
					throw new Exception(hyphenPart
							+ " は有効なオプションではありません. -h でヘルプが表示されます．");
				}
				addParameter(hyphenPart, val); // セットで追加
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * パラメータ設定を List で受け取る場合のメソッド．<br>
	 * 単に List を配列化して setParameter に渡すだけ．
	 * @param params パラーメタリスト．
	 */
	public void setParameter(List<String> params) {
		String[] array = new String[params.size()];
		params.toArray(array);
		// 配列版のパラメータ設定関数を呼ぶ．
		setParameter(array);
	}

	/**
	 * パラメータの追加.
	 * @param hyphenPart キー
	 * @param val 値
	 */
	private void addParameter(String hyphenPart, String val) {
		// パラメータマップ
		HashMap<String, String> map = getGAParams().getParametersMap();
		// 選択
		if (hyphenPart.equals("-S")) {
			map.put(GAParameters.SELECTION, val);
			// 交叉
		} else if (hyphenPart.equals("-C")) {
			map.put(GAParameters.CROSSOVER, val);
			// 突然変異
		} else if (hyphenPart.equals("-M")) {
			map.put(GAParameters.MUTATION, val);
			// 選択パラメータ
		} else if (hyphenPart.equals("-Sparam")) {
			map.put(GAParameters.S_PARAM, val);
			// 交叉パラメータ
		} else if (hyphenPart.equals("-Cparam")) {
			map.put(GAParameters.C_PARAM, val);
			// 突然変異パラメータ
		} else if (hyphenPart.equals("-Mparam")) {
			map.put(GAParameters.M_PARAM, val);
			// 目的関数で使う変換関数
		} else if (hyphenPart.equals("-scaling1")) {
			map.put(GAParameters.FUNCTION, val);
			// 目的関数で使う変換関数パラメータ
		} else if (hyphenPart.equals("-scaling1param")) {
			map.put(GAParameters.FUNCTION_PARAMETER, val);
			// 選択で使うスケーリング
		} else if (hyphenPart.equals("-scaling2")) {
			map.put(GAParameters.TRANSFORM, val);
			// 選択で使うスケーリングパラメータ
		} else if (hyphenPart.equals("-scaling2param")) {
			map.put(GAParameters.TRANSFORM_PARAMETER, val);
			// 問題
		} else if (hyphenPart.equals("-P")) {
			map.put(GAParameters.PROBLEM, val);
			// 問題パラメータ
		} else if (hyphenPart.equals("-Pparam")) {
			map.put(GAParameters.PROBLEM_PARAMETER, val);
			// 個体数
		} else if (hyphenPart.equals("-popsize")) {
			map.put(GAParameters.POPULATION_SIZE, val);
			// 世代数
		} else if (hyphenPart.equals("-gsize")) {
			map.put(GAParameters.GENERATION_SIZE, val);
			// 遺伝子長
		} else if (hyphenPart.equals("-clength")) {
			map.put(GAParameters.CHROMOSOME_LENGTH, val);
			// プリントレベル
		} else if (hyphenPart.equals("-printlevel")) {
			map.put(GAParameters.PRINT_LEVEL, val);
			// 出力ファイル
		} else if (hyphenPart.equals("-output")) {
			map.put(GAParameters.OUTPUT_FILE, val);
			// エリートの有無
		} else if (hyphenPart.equals("-elitism")) {
			map.put(GAParameters.IS_ELITISM, val);
			// ビューワ
		} else if (hyphenPart.equals("-viewer")) {
			map.put(GAParameters.VIEWER, val);
			// 高速化フラグ
		} else if (hyphenPart.equals("-turbo")) {
			map.put(GAParameters.IS_TURBO, val);
			// 個体メモリフラグ
		} else if (hyphenPart.equals("-memory")) {
			map.put(GAParameters.IS_MEMORY, val);
		} else if (hyphenPart.equals("-seed")) {
			map.put(GAParameters.SEED, val);
		} else {
			throw new IllegalArgumentException(hyphenPart + " is wrong!");
		}
	}

	/**
	 * ヘルプを表示
	 */
	public void printHelp() {
		System.err.println("使用可能なオプションは以下の通りです:");
		System.err.println("  -help / -h	ヘルプ表示");
		System.err.println("  -S		選択指定 例: operator.TournamentSelection");
		System.err.println("  -C		交叉指定 例: operator.UniformCrossover");
		System.err.println("  -M		突然変異指定 例: operator.BitMutation");
		System.err.println("  -Sparam	選択パラメータ 例: 3，2:4 (複数のパラメータは:で区切る)");
		System.err.println("  -Cparam	交叉パラメータ 例: 0.6 (複数のパラメータは:で区切る)");
		System.err.println("  -Mparam	突然変異パラメータ 例: 0.01 (複数のパラメータは:で区切る)");
		System.err
				.println("  -scaling1	目的関数で使う変換関数 例: problem.function.LinearFunction");
		System.err
				.println("  -scaling2	選択で使うスケーリング 例: operator.transform.RankTransform");
		System.err.println("  -P		問題 例: problem.KnapsackProblem");
		System.err.println("  -Pparam	問題パラメータ 例: 10，20:3 (複数のパラメータは:で区切る)");
		System.err.println("  -popsize	個体数 例: 100");
		System.err.println("  -gsize	世代数 例: 200");
		System.err.println("  -clength	遺伝子長 例: 30");
		System.err.println("  -printlevel	プリントレベル 例: 1");
		System.err
				.println("  -output	出力先 例: log.txt, stderr(標準出力)，stderr(標準エラー出力)");
		System.err.println("  -elitism	エリート主義 例: true/false");
		System.err.println("  -viewer	ビューワ 例: viewer.TextViewer");
		System.err.println("  -turbo	高速化フラグ 例: true/false");
		System.err.println("  -memory	個体メモリフラグ 例: true/false");
		System.err.println("  -seed	シード 例: 0 (指定しなければシステム時刻を使用)");
	}

	/**
	 * 各GAパラメータを返す.
	 * @return GAパラメータ
	 */
	public GAParameters getGAParams() {
		if (gaParams_ == null) {
			gaParams_ = new GAParameters();
		}
		return gaParams_;
	}

	/**
	 * CSVファイルによる初期化
	 * @param filename CSVファイル名
	 */
	public void setGAParamsByCSVFile(String filename) {
		String line;
		HashMap<String, String> map = getGAParams().getParametersMap();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				// 空行スキップ
				if (line.matches("^\\s*$")) {
					continue;
				}
				// ＃から始まるコメント行除去
				if (line.matches("^#.*")) {
					continue;
				}
				// CSVを前提にしているので , がない行は無視
				if (!line.matches(".*,.*")) {
					continue;
				}
				// 空白除去
				line = line.replaceAll("\\s", "");
				String[] data = line.split(",");
				// key,val の形式になっていないもの
				if (data.length != 2) {
					continue;
				}
				map.put(data[0], data[1]);
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void localTest() throws LocalTestException {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		// Director()のテスト
		if (d.builder_ != builder) {
			throw new LocalTestException(" Director constractor error! ");
		}
		// setParameter(String[])のテスト
		IGABuilder builder2 = new DefaultGABuilder();
		Director d2 = new Director(builder2);
		try{
			// null を渡すのは OK.
			d2.setParameter((String[]) null);
		} catch (Exception e) {
			throw new LocalTestException(e.getMessage());			
		}
		String[] param = { "", "" };
		try {
			// 空文字列はダメ．
			d2.setParameter(param);
			// 例外が発生していないとおかしい			
			throw new LocalTestException();
		} catch (Exception e) {

		}
		param = new String[] { "-P" };
		try {
			// 不完全なものもダメ．
			d2.setParameter(param);
			// 例外が発生していないとおかしい
			throw new LocalTestException();
		} catch (Exception e) {

		}
		param = new String[] { "-P", "problem.BitCountProblem" };
		try{
			// これは OK.
			d2.setParameter(param);
			d2.constract();
			builder2.buildGA();
		}catch(Exception e){
			throw new LocalTestException(e.getMessage());			
		}
		// getGAParams()のテスト
		Director d3 = new Director(new DefaultGABuilder());
		if (d3.getGAParams() == null) {
			throw new LocalTestException();
		}
	}
}
