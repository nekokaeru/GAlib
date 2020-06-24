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

/**
 * @author mori
 * @version 0.1 05/09/13
 */
package population;

import java.util.Arrays;

import util.MyRandom;
import fitness.ChangedFlagAndMemoryFitness;
import fitness.FitnessManager;
import fitness.IFitness;
import gabuilder.GAParameters;

/**
 * �̃N���X�D�ȉ����F�̂ƈ�`�q�^�͓����Ӗ��ŗp���Ă���D
 * @author mori
 * @version 1.0
 */
public class Individual implements Cloneable {
	/**
	 * ���F�́D�������̂��߂ɔz����g���D
	 */
	private Number[] chromosome_;

	/**
	 * �ύX�����������̃t���O
	 */
	private boolean isChanged_ = true;

	/**
	 * �ȑO�̓K���x
	 */
	private double previousFitness_ = Double.NaN;

	/**
	 * �g�����ꂽ�K���x�v�Z�N���X
	 */
	private static IFitness fitnessCalculator_;

	/**
	 * ��`�q�^�\�����̈�`�q�Ԃ̋�؂蕶��
	 */
	private static String separator_ = "";

	/**
	 * �����Ȃ��R���X�g���N�^
	 */
	public Individual() {

		chromosome_ = new Number[GAParameters.DEFAULT_CHROMOSOME_LENGTH];
		init();
	}

	/**
	 * �������R���X�g���N�^
	 * @param size ��`�q��
	 */
	public Individual(int size) {
		chromosome_ = new Number[size];
		init();
	}

	/**
	 * ���F�̂ŏ�����. ���F�̎��̂͐󂢃R�s�[�ɂȂ��Ă��邪 Integer, Double �Ȃǂ̕s�σI�u�W�F�N�g�̔z��̏ꍇ�͖��Ȃ�.
	 * Number ���p����������̃N���X��p����ꍇ�͕s�σI�u�W�F�N�g�ł���K�v������D
	 * @param chromosome ���F��
	 */
	public Individual(Number[] chromosome) {
		setChromosome(chromosome);
	}

	/**
	 * ���F��(������)�ŏ����� "10010" �Ȃ�. ���l�ȊO���܂�ł���ꍇ�ɂ͗�O�����D TODO
	 * ���͈�`�q���ꌅ�����ʖځD�K�v������΃Z�p���[�^��ݒ肵���o�[�W���������D
	 * @param chromStr ��`�q�̕�����\��
	 */
	public Individual(String chromStr) {
		// String �� matches �͊��S��v�D \\D �������ƈꕶ���̏ꍇ�݂̂Ȃ̂őʖځD
		if (chromStr.matches(".*\\D.*")) {
			// �������������Ȃ��D
			throw new IllegalArgumentException(
					"A gene must be numerical value!");
		}
		String[] array = chromStr.split("\\B");// ��P�ꋫ�E�ŕ���
		Number[] chromosome = new Number[array.length];
		for (int i = 0; i < array.length; i++) {
			chromosome[i] = Integer.parseInt(array[i]);
		}
		chromosome_ = chromosome;
	}

	/**
	 * �R�s�[�R���X�g���N�^
	 * @param indiv �R�s�[�Ώی�
	 */
	public Individual(Individual indiv) {
		chromosome_ = new Number[indiv.chromosome_.length];
		System.arraycopy(indiv.chromosome_, 0, chromosome_, 0,
				chromosome_.length);
		// �K���x�̍X�V�����󂯌p��
		setChanged(indiv.isChanged());
		setPreviousFitness(indiv.getPreviousFitness());
	}

	/**
	 * �����_���ɐ��F�̂��������D���݂͑Η���`�q {0,1} �݂̂ɑΉ��D TODO �Η���`�q��3�ȏ�̏ꍇ�̎����D
	 */
	public void init() {
		for (int i = 0; i < chromosome_.length; i++) {
			chromosome_[i] = MyRandom.getInstance().nextInt(2);
		}
	}

	/**
	 * �[���R�s�[�����邽�߂ɁCclone ���I�[�o�[���C�h�D JDK5.0 �̋��ϖ߂�l�𗘗p�D �������CNumber
	 * �I�u�W�F�N�g�͐󂢃R�s�[�ɂȂ��Ă��邪�CInteger�CDouble ���� �s�σI�u�W�F�N�g�̏ꍇ�͖��Ȃ��D
	 * @return �[���R�s�[���ꂽ��
	 */
	@Override
	public Individual clone() {
		try {
			Individual i = (Individual) super.clone();
			Number[] tmp = new Number[size()];
			System.arraycopy(chromosome_, 0, tmp, 0, size());
			i.setChromosome(tmp);
			// �K���x�̍X�V�����󂯌p��
			i.setChanged(isChanged());
			i.setPreviousFitness(getPreviousFitness());
			return i;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e.getMessage());
		}
	}

	/**
	 * equals �̎����DJava�̓S�� 12 �Q�Ƃ��قȂ��Ă��Ă���`�q�^�̓��e����������Γ��l�Ƃ���D
	 * �������C�p���ɂ���ăN���X���Ⴄ�ꍇ�ɂ͏�ɋU��Ԃ��D�Ȃ��C���� 1 �ł��^���قȂ�� equals �̌��ʂ͋U�ɂȂ�D
	 * @return ���F�̂��������e�Ȃ� true �Ⴄ�Ȃ� false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if ((obj != null) && (getClass() == obj.getClass())) {
			return Arrays.equals(chromosome_, ((Individual) obj).chromosome_);
		}
		return false;
	}

	/**
	 * ���F�̂̃n�b�V���l��Ԃ��D ���F�̂������Ȃ瓯���l��Ԃ��D
	 * @return ���F�̂��n�b�V���l
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(chromosome_);
	}

	/**
	 * ���F�̂�Ԃ��D
	 * @return ���F��
	 */
	public final Number[] getChromosome() {
		return chromosome_.clone();
	}

	/**
	 * �w�肵����`�q���̈�`�q��Ԃ��D
	 * @return �w�肳�ꂽ��`�q
	 */
	public final Number getGeneAt(int index) {
		return chromosome_[index];
	}

	/**
	 * ���F�̂��Z�b�g���C�ϊ��t���O�� true �ɂ���D
	 * @param chromosome ���F��
	 */
	public final void setChromosome(Number[] chromosome) {
		// clone �ȂǓ������F�̂ł��X�V�̕K�v����D
		setChanged(true); // �ύX���������D
		chromosome_ = new Number[chromosome.length];
		// choromosome_ �� chromosome �͓��� Number �I�u�W�F�N�g���w�����C
		// Integer ���̕s�σI�u�W�F�N�g�ł���Ζ��Ȃ��D
		System.arraycopy(chromosome, 0, chromosome_, 0, chromosome.length);
	}

	/**
	 * ���F�̂� index �ɂ����镔���� value �ɕς���D �ς���ꂽ�Ƃ��́C�ϊ��t���O�� true �ɂ���D
	 * @param index ��`�q��
	 * @param value ��`�q
	 */
	public final void setGeneAt(int index, Number value) {
		if (!chromosome_[index].equals(value)) {
			setChanged(true); // �ύX���������D
			chromosome_[index] = value;
		}
	}

	/**
	 * ���F�̂̈�`�q����Ԃ��D
	 * @return ��`�q��
	 */
	public final int size() {
		return chromosome_.length;
	}

	/**
	 * ���F�̂� String �ɕϊ����ĕԂ��D
	 * @return ���F�́i������j
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			sb.append(chromosome_[i]);
			if (i < size() - 1) {
				sb.append(getSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * �̂̓K���x��Ԃ��D �I�����ɕK�v�ȃX�P�[�����O�͓K�p����Ă��Ȃ��l�ł���D �G���[�g�͖���ۑ������͂���D<br>
	 * �f�t�H���g�� ChangedFlagAndMemoryFitness �� fitnessCalculator_ �Ȃ̂�
	 * �������t���O���ݒ肳��Ă���ꍇ�́C��`�I������󂯂Ă��Ȃ��̂̓t���O�𗘗p���� previousFitness �̒l��Ԃ��D
	 * �L���t���O���ݒ肳��Ă���ꍇ�́C��`�I������󂯂� MemoryBank �ɕۑ�����Ă���΁C
	 * FitnessManager.getMemoryBank().getFitness(indiv) �œK���x��Ԃ��D
	 * @return �K���x
	 * @see FitnessManager#getMemoryBank()
	 */
	public double fitness() {
		double f = getFitnessCalculator().fitness(this); // �K���x�𓾂�D
		FitnessManager.updateElite(this, f);// elite ���X�V����D
		return f;
	}

	/**
	 * ��������L���������g�p���Ȃ��C�V���v���ȏ]���̓K���x�֐��D �G���[�g���ۑ����Ȃ��D<br>
	 * �������C�K���x�]���񐔂̓J�E���g�����D
	 * @return �K���x
	 */
	public double simpleFitness() {
		return FitnessManager.getFitness(chromosome_);
	}

	/**
	 * �K���x���v�Z����D ��{�I�ɂ� simpleFitness �Ɠ��������C�K���x�]���񐔂��J�E���g����Ȃ��D<br>
	 * �r���̃��O�ȂǒT���Ɋ֌W�̂Ȃ��K���x�v�Z�̂��߂̕⏕�I�ȃ��\�b�h.
	 * @return �K���x
	 * @see FitnessManager#getFitnessWithoutRecord(Number[])
	 */
	public double fitnessWithoutRecord() {
		return FitnessManager.getFitnessWithoutRecord(chromosome_);
	}

	/**
	 * ���̓K���x�C�܂�ړI�֐��l���̂��̂�Ԃ�. �K���x�]���񐔂̓J�E���g����Ȃ��D
	 * @return ���̓K���x(�ړI�֐��l)
	 */
	public double rawFitness() {
		return FitnessManager.getProblem().getObjectiveFunctionValue(
				chromosome_);
	}

	/**
	 * ���F�̂̕ϊ��̗L���𒲂ׂ�t���O��Ԃ��D ���F�̂���`�I������󂯂����Ctrue ��Ԃ��D
	 * @return ���F�̂̈�`�I����̗L���D ��`�I������󂯂����� true ��Ԃ��C�󂯂Ă��Ȃ����� false ��Ԃ��D
	 */
	public final boolean isChanged() {
		return isChanged_;
	}

	/**
	 * ���F�̂̕ϊ��̗L���𒲂ׂ�t���O���Z�b�g����D ���F�̂���`�I������󂯂����Ctrue �ɂȂ�D
	 * @param isChanged
	 */
	public final void setChanged(boolean isChanged) {
		this.isChanged_ = isChanged;
	}

	/**
	 * ���O�̓K���x��Ԃ��D
	 * @return �O�̓K���x
	 */
	public final double getPreviousFitness() {
		return previousFitness_;
	}

	/**
	 * �ŐV�̓K���x���Z�b�g����D
	 * @param previousFitness �O�̓K���x
	 */
	public final void setPreviousFitness(double previousFitness) {
		previousFitness_ = previousFitness;
	}

	/**
	 * �K���x���v�Z����C���X�^���X��Ԃ��D
	 * @return �K���x���v�Z����C���X�^���X
	 */
	public static IFitness getFitnessCalculator() {
		if (fitnessCalculator_ == null) {
			fitnessCalculator_ = new ChangedFlagAndMemoryFitness();
		}
		return fitnessCalculator_;
	}

	/**
	 * �K���x���v�Z����C���X�^���X���Z�b�g����.
	 * @param fitnessCalculator �K���x���v�Z����C���X�^���X
	 */
	public static void setFitnessCalculator(IFitness fitnessCalculator) {
		fitnessCalculator_ = fitnessCalculator;
	}

	/**
	 * ��`�q�^�\�����̋�؂蕶���𓾂�D
	 * @return ��؂蕶��
	 */
	public static String getSeparator() {
		return separator_;
	}

	/**
	 * ��`�q�^�\�����̋�؂蕶����ݒ肷��D
	 * @param s ��؂蕶��
	 */
	public static void setSeparator(String s) {
		separator_ = s;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		Individual indiv = new Individual(new Number[] { 1, 1, 1, 0, 0 });
		// �f�t�H���g���ł���r�b�g�J�E���g�œK���x���Z�o.
		System.out.println(indiv.fitness());
		Individual indiv2 = new Individual(new Number[] { 1, 0, 1 });
		Individual indiv3 = new Individual(new Number[] { 1, 0, 1 });
		Individual indiv4 = new Individual(new Number[] { 1, 0, 1L });
		// indiv2 �� indiv3 �͓���̐��F�̂������ߕ\���� true.
		System.out.println(indiv2.equals(indiv3));
		// indiv2 �� indiv4 �͍Ō�̈�`�q�� Integer �� Long �ňقȂ邽�ߕ\����false.
		System.out.println(indiv2.equals(indiv4));
	}
}
