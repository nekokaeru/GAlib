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

package viewer;

import fitness.FitnessManager;
import gabuilder.GABase;
import gabuilder.GAParameters;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import population.Individual;
import population.Population;
import population.PopulationAnalyzer;
import util.MyRandom;

/**
 * �����\���ɂ��r���[���N���X.
 * @author mori
 * @version 1.0
 */

public class TextViewer implements IViewer {
	/**
	 * GABase �ւ̎Q�ƁD MVC �̃��f���D
	 */
	private GABase ga_;

	/**
	 * �\�����x��
	 */
	private int printLevel_ = GAParameters.DEFAULT_PRINT_LEVEL;

	/**
	 * �o�̓X�g���[��
	 */
	private PrintStream out_;

	/**
	 * �o�̓t�@�C�����D�󕶎���̏ꍇ�͕W���o�͂ɕ\��.
	 */
	private String filename_ = "";

	/**
	 * �J�n���Ɉ�x�����\��������e���L�q�D
	 */
	public void begin() {
		// printLevel_ < 0 �̏ꍇ�����\�����Ȃ��D
		if (printLevel_ < 0) {
			return;
		}
		getOut().println(new Date());
		getOut().println("Problem: " + FitnessManager.getProblem().toString());
		getOut().println(
				"seed:" + MyRandom.getSeed() + " turbo:"
						+ FitnessManager.isTurbo() + " memory:"
						+ FitnessManager.isMemory());
		getOut().print("Operators:\n" + getGA().getOperatorInfo());
		getOut().println(
				"population size:"
						+ getGA().getPopulation().getPopulationSize()
						+ " chromosome length:"
						+ getGA().getPopulation().getIndividualAt(0).size());
		// printLevel_ ==3 �̏ꍇ�͏����̌Q���\������D
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 �̏ꍇ�͏����̌Q+�K���x��\������D
		} else if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}

		getOut().println("---------begin---------");
	}

	/**
	 * �I�����Ɉ�x�����s���鏈�����L�q�D
	 */
	public void end() {
		// printLevel_ < 0 �̏ꍇ�����\�����Ȃ��D
		if (printLevel_ < 0) {
			return;
		}
		getOut().println("----------end----------");
		getOut().println(new Date());
		getOut().println(
				"Total fitness evaluation:" + FitnessManager.getTotalEvalNum());
		// printLevel_ ==3 �̏ꍇ�͍ŏI�̌Q���\������D
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 �̏ꍇ�͍ŏI�̌Q+�K���x���\������D
		}
		if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}
		printElite();
		// �X�g�[�����̌㏈��
		if (filename_.equals("")) {
			// �W���o�͂̏ꍇ�� flush �ŏI��
			getOut().flush();
		} else {
			// �t�@�C���o�͂̏ꍇ�� close �ŏI��
			getOut().close();
		}
	}

	/**
	 * �\���̍X�V
	 * @param info �t�����
	 */
	public void update(String info) {
		HashMap<String, Double> fitInfo = null;
		// printLevel_ <= 0 �̏ꍇ�����\�����Ȃ��D
		if (printLevel_ <= 0) {
			return;
		}
		if (printLevel_ >= 1) {
			// begin: printLevel_ >= 1 �̏ꍇ�ɕ\�����镔���D
			fitInfo = PopulationAnalyzer.fitnessInfo(getGA().getPopulation());
			getOut().println(info + "maxFitness:" + fitInfo.get("max"));
		}
		if (printLevel_ >= 2) {
			getOut().println(
					"meanFitness:"
							+ fitInfo.get("mean")
							+ " minFitness:"
							+ fitInfo.get("min")
							+ " entropy:"
							+ PopulationAnalyzer.entropy(getGA()
									.getPopulation())
							+ " genotypeNum:"
							+ PopulationAnalyzer.genotypeNum(getGA()
									.getPopulation()) + " fitness evalNum:"
							+ FitnessManager.getTotalEvalNum());

		}
		// printLevel_ ==3 �̏ꍇ�͏����̌Q���\������D
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 �̏ꍇ�͏����̌Q+�K���x���\������D
		}
		if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}
		return;
	}

	/**
	 * �̌Q��\���D
	 * @param pop �̌Q
	 */
	public void printPopulation(Population pop) {
		getOut().print(pop);
	}

	/**
	 * �G���[�g�̂�\��.
	 */
	public void printElite() {
		ArrayList<Individual> list = FitnessManager.getCopyOfEliteList();
		getOut().println("----elite----");
		getOut().println("elite fitness: " + FitnessManager.getEliteFitness());
		for (Individual individual : list) {
			getOut().println(individual);
		}
		getOut().println("-------------");
	}

	/**
	 * �o�̓X�g���[����Ԃ�.
	 * @return �o�̓X�g���[��
	 */
	public PrintStream getOut() {
		if (out_ == null) {
			if (filename_.equals("") || filename_.equalsIgnoreCase("STDOUT")) {
				// �W���o�͂�Ԃ��D
				out_ = System.out;
			} else if (filename_.equalsIgnoreCase("STDERR")) {
				// �W���G���[�o�͂�Ԃ��D
				out_ = System.err;

			} else {
				try {
					out_ = new PrintStream(new File(getFilename()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return out_;
	}

	/**
	 * �o�̓X�g���[����ݒ�.
	 * @param out �o�̓X�g���[��
	 */
	public void setOut(PrintStream out) {
		out_ = out;
	}

	/**
	 * �o�̓t�@�C������Ԃ�.
	 * @return �o�̓t�@�C����
	 */
	public String getFilename() {
		return filename_;
	}

	/**
	 * �o�̓t�@�C������ݒ�.
	 * @param filename �o�̓t�@�C����
	 */
	public void setFilename(String filename) {
		filename_ = filename;
	}

	/**
	 * GABase �ւ̎Q�Ƃ�Ԃ�.
	 * @return GABase �ւ̎Q��
	 */
	public GABase getGA() {
		return ga_;
	}

	/**
	 * GABase �ւ̎Q�Ƃ̐ݒ�.
	 * @param ga GA�ւ̎Q��
	 */
	public void setGA(GABase ga) {
		ga_ = ga;
	}

	/**
	 * ���O��Ԃ��D
	 * @see viewer.IViewer#getName()
	 */
	public String getName() {
		return "TextViewer";
	}

	/**
	 * �\�����x����Ԃ�.
	 * @return �\�����x��
	 */
	public int getPrintLevel() {
		return printLevel_;
	}

	/**
	 * �\�����x���̐ݒ�.
	 * @param printLevel �\�����x��
	 */
	public void setPrintLevel(int printLevel) {
		printLevel_ = printLevel;
	}
}
