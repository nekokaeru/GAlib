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

package sample;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * CLike_SGA �̃e�X�g�D�̐� 10�C��`�q�� 10 �ɓ������Ă���̂Œ��ӁD
 * @author mori and takeuchi
 * @version 1.0
 */
public class CLike_SGATest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CLike_SGATest.class);
	}

	public CLike_SGATest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @see sample.CLike_SGA#CLike_SGA()
	 */
	public void testSGA() {
		CLike_SGA g = new CLike_SGA();
		// ��`�q��10
		assertEquals(10, g.LENGTH);
		for(int i = 0; i < g.pop_.length; i++){
			assertEquals(10, g.pop_[i].length);
		}
		// �̐�10
		assertEquals(10, g.POPSIZE);
		assertEquals(10, g.pop_.length);
		// �ˑR�ψٗ�0.1
		assertEquals(0.1, g.MRATE, 0);
		// ������0.6
		assertEquals(0.6, g.CRATE, 0);
		// ���㐔100
		assertEquals(100, g.GENERATION_SIZE);
	}

	/**
	 * @see sample.CLike_SGA#printPop()
	 */
	public void testPrintPop() {

	}

	/**
	 * @see sample.CLike_SGA#setElite()
	 */
	public void testSetElite() {
		CLike_SGA g = new CLike_SGA();
		for(int i = 0; i < g.pop_.length; i++) {
			g.pop_[i][0] = 0;//���ׂĂ̑���`�q����0�ɂ���D�œK���� all 1.
		}
		g.setElite();//���ׂĂ̓K���x���v�Z��, �G���[�g���X�V.
		//�n�߂̈�`�q�� 0 �Ȃ̂ŕK���G���[�g�̓K���x��10�ȉ��D
		assertTrue(g.fitness(g.elite_)<10);
		int[] opt = new int[]{1,1,1,1,1,1,1,1,1,1}; //�œK���D
		g.pop_[0] = opt; //�œK�̂��Z�b�g�D
		g.setElite();//���ׂĂ̓K���x���v�Z��, �G���[�g���X�V�D
		assertEquals(g.fitness(g.elite_),10,0);//�G���[�g�̓K���x�͓��R10
		g.pop_[0][0] = 0; //�œK�����󂷁D
		assertEquals(g.fitness(g.elite_),10,0);//�G���[�g�̓K���x��10�̂܂�
		//���ׂĂ̓K���x���v�Z�D
		for (int i = 0; i < g.POPSIZE; i++) {
			assertTrue(g.fitness(g.pop_[i])<10); //�̌Q���ɂ͍œK���͂Ȃ��D
		}
	}

	/**
	 * @see sample.CLike_SGA#start()
	 */
	public void testStart() {

	}

	/**
	 * @see sample.CLike_SGA#crossover()
	 */
	public void testCrossover() {
		CLike_SGA g = new CLike_SGA();
		int[][] pop = new int[g.POPSIZE][g.LENGTH];
		// �̌Q�̐擪��2�̂��ȉ���c1, c2�ɒu��������.
		int[] c1 = new int[g.LENGTH];
		int[] c2 = new int[g.LENGTH];
		Arrays.fill(c1, 1);
		Arrays.fill(c2, 0);
		System.arraycopy(c1, 0, g.pop_[0], 0, g.LENGTH);
		System.arraycopy(c2, 0, g.pop_[1], 0, g.LENGTH);
		for(int i = 0; i < g.POPSIZE; i++){
			System.arraycopy(g.pop_[i],0,pop[i],0,g.LENGTH);
		}
		g.POPSIZE = 2;
		int maxi = 3000;
		int nsig = 4;
		int sum = 0;
		// �̌Q�̐擪��2�̂̂݌����ɎQ���\
		for (double crate = 0; crate <= 1; crate += 0.2) {
			g.CRATE = crate;
			sum = 0;
			for (int i = 0; i < maxi; i++) {
				// �����O�̌̌Q�ɏ�����
				for(int j = 0; j < g.POPSIZE; j++){
					System.arraycopy(pop[j],0,g.pop_[j],0,g.LENGTH);
				}
				g.crossover();
				if (Arrays.equals(g.pop_[0],pop[0])) {
					// �������N���Ă��Ȃ���ΐ��F�̂ɕω��Ȃ��D
					assertTrue(Arrays.equals(g.pop_[1],pop[1]));
				} else {
					// �������N���Ă���ΐ��F�̂��ω��D
					assertFalse(Arrays.equals(g.pop_[1],pop[1]));
					sum++; // �������N�����񐔁D
				}
			}
			double E = g.CRATE * maxi;	// ���Ғl
			double SD = Math.sqrt(E * (1.0 - g.CRATE));	// �W���΍�
			assertTrue(Math.abs(sum - E) <= nsig * SD);
		}
	}

	/**
	 * @see sample.CLike_SGA#mutation()
	 */
	public void testMutation() {
		CLike_SGA g = new CLike_SGA();
		int[] indiv0_c = new int[g.LENGTH];
		Arrays.fill(indiv0_c, 0);
		// �̌Q�̑S�̂̑S��`�q��0�ɂ���
		for(int i = 0; i < g.POPSIZE; i++){
			System.arraycopy(indiv0_c, 0, g.pop_[i], 0, g.LENGTH);
		}
		// �ˑR�ψٗ�0�̏ꍇ�͕ω��Ȃ�
		g.MRATE = 0.0;
		g.mutation();
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < g.LENGTH; j++){
				assertFalse(g.pop_[i][j] == 1);
			}
		}
		// �ˑR�ψٗ�1�̏ꍇ�͂��ׂĕω�
		g.MRATE = 1.0;
		g.mutation();
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < g.LENGTH; j++){
				assertFalse(g.pop_[i][j] == 0);
			}
		}
		double mrate;
		double[] sum0 = new double[g.LENGTH];
		int maxi = 10000;
		double nsig = 4; //���V�O�}�܂Œ��ׂ邩�D
		for (mrate = 0.2; mrate < 1; mrate += 0.2) {
			g.MRATE = mrate;
			Arrays.fill(sum0, 0);
			for (int i = 0; i < maxi; i++) {
				// �ˑR�ψّO�̌̌Q�ɏ�����
				Arrays.fill(indiv0_c, 0);
				for(int j = 0; j < g.POPSIZE; j++){
					System.arraycopy(indiv0_c, 0, g.pop_[j], 0, g.LENGTH);
				}
				g.mutation();
				for (int j = 0; j < g.pop_[0].length; j++) {
					sum0[j] += g.pop_[0][j];
				}
			}
			for (int j = 0; j < g.pop_[0].length; j++) {
				// �m���I�� false �ɂȂ邱�Ƃ���. 4�Г��ɂ��邱�Ƃ𒲂ׂ�D
				double E = g.MRATE * maxi;	// ���Ғl
				double SD = Math.sqrt(E * (1 - g.MRATE));	// �W���΍�
				assertTrue(Math.abs(sum0[j]-E)<=nsig*SD);
			}
		}
	}

	/**
	 * @see sample.CLike_SGA#selection()
	 */
	public void testSelection() {
		CLike_SGA g = new CLike_SGA();
		// �̌Q�̊e�̂̃C���f�b�N�X�ƓK���x���������Ȃ�悤�Ɉ�`�q��ύX
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < i % g.LENGTH; j++){
				g.pop_[i][j] = 1;	// �C���f�b�N�X��������`�q���P
			}
			for(int j = i; j < g.LENGTH; j++){
				g.pop_[i][j] = 0;	// ���̑���0
			}
		}
        int[] result = new int[g.POPSIZE];
        double sum = 0;
        int maxi = 3000;
        double nsig = 4; //���Ђ܂Œ��ׂ邩�D
        for (int i = 0; i < g.POPSIZE; i++) {
            sum+=g.fitness(g.pop_[i]);	// ���K���x
            result[i]=0;
        }
        int[] indexArray = new int[g.POPSIZE];
        for (int i = 0; i < maxi; i++) {
        	// �I��O�̌̌Q�ɏ�����
        	for(int k = 0; k< g.POPSIZE;k++){
    			for(int j = 0; j < k % g.LENGTH; j++){
    				g.pop_[k][j] = 1;
    			}
    			for(int j = k; j < g.LENGTH; j++){
    				g.pop_[k][j] = 0;
    			}
    		}
            g.selection();
            for(int j = 0; j < g.POPSIZE; j++){
            	// �I�΂ꂽ�̂̓K���x�܂�C���f�b�N�X��Ԃ�
            	indexArray[j] = g.fitness(g.pop_[j]);
            }
            for(int j = 0; j < indexArray.length; j++){
            	// �e�C���f�b�N�X���Ƃ̑I�����ꂽ�񐔂��J�E���g
                result[indexArray[j]]++;
            }
        }
        //�K���x�O�̌̂͑I�΂�Ȃ�
        assertTrue(result[0]==0);
        for (int i = 1; i < result.length; i++) {
            //�m���I�� false �ɂȂ邱�Ƃ���
        	double p = i/sum; // i �Ԗڂ��I�΂��m���D
        	double E = p * maxi *  g.POPSIZE; //���Ғl
        	double SD = Math.sqrt(E * (1 - p)); //�W���΍�
            assertTrue(Math.abs(result[i]-E)< nsig*SD);
        }
	}

	/**
	 * @see sample.CLike_SGA#returnElite()
	 */
	public void testReturnElite() {
		CLike_SGA g = new CLike_SGA();
		// �̌Q�̊e�̂̃C���f�b�N�X�ƓK���x���������Ȃ�悤�Ɉ�`�q��ύX
		// �܂�̌Q�̍ŏ��K���x��0, �ő�K���x��9
		double[] data={0,1,2,3,4,5,6,7,8,9};
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < data[i]; j++){
				g.pop_[i][j] = 1;
			}
			for(int j = (int)data[i]; j < g.LENGTH; j++){
				g.pop_[i][j] = 0;
			}
		}
		int[] opt = new int[]{1,1,1,1,1,1,1,1,1,1}; //�œK���D
		g.elite_ = opt; //�œK�̂��G���[�g�ɃZ�b�g�D
		g.returnElite();	// �G���[�g���̌Q�ɖ߂�
		// �K���x���ŏ��̌̂ƃG���[�g�����������
		assertEquals(10.0, g.fitness(g.pop_[0]), 0);
	}

	/**
	 * @see sample.CLike_SGA#fitness(int[])
	 */
	public void testFitness() {
		CLike_SGA g = new CLike_SGA();
		int[] c1 = new int[g.POPSIZE];
		int[] c2 = new int[g.POPSIZE];
		Arrays.fill(c1, 0);
		Arrays.fill(c2, 1);
		// �ŏ��K���x0
		assertEquals(0, g.fitness(c1));
		// �ő�K���x10
		assertEquals(10, g.fitness(c2));
	}

}
