Feature: �t�B�[�`���t�@�C������e�X�g���{���ʕ񍐏��𐶐�����
* �e�X�g�d�l���̓��r���[�⍷���m�F���e�ՂɂȂ邽�߁A�e�L�X�g�t�@�C���ō쐬������
* ���{�ƌ��ʂ̕񍐂̓e�L�X�g�t�@�C���ł͓���̂łЂƂ܂��G�N�Z���ł܂Ƃ߂���
* �e�X�g�d�l�����玩���Ō��ʕ񍐃t�@�C���𐶐�������

  # �����ɃR�����g�������Ɣ��l�ɂȂ�܂��B
  # ������񕡐��̃R�����g���������Ƃ�
  # �\�ł��B

  Background:
    Given example.feature �� /tmp/vinegar �ɃR�s�[����
    Given /tmp/vinegar/example.feature �� UTF-8 �ɂ���

  Scenario: �R�}���h�����s���Č��ʕ񍐏��𐶐�����
    Given �z�[���f�B���N�g���Ɉړ�����
    When "vinegar /tmp/vinegar/example.feature" �����s����
    Then /tmp/vinegar �� example.xlsx �t�@�C�������݂��邱��
    Then �ΏۃX�g�[���[�Ɂu�t�B�[�`���t�@�C������e�X�g���{���ʕ񍐏��𐶐�����v�ƕ\������Ă��邱��
    Then �ړI�^�ϓ_�Ɉȉ����\������Ă��邱��:
      """
      * �e�X�g�d�l���̓��r���[�⍷���m�F���e�ՂɂȂ邽�߁A�e�L�X�g�t�@�C���ō쐬������
      * ���{�ƌ��ʂ̕񍐂̓e�L�X�g�t�@�C���ł͓���̂łЂƂ܂��G�N�Z���ł܂Ƃ߂���
      * �e�X�g�d�l�����玩���Ō��ʕ񍐃t�@�C���𐶐�������
      """
    Then ���O�����Ɉȉ����\������Ă��邱��:
      """
      example.feature �� /tmp/vinegar �ɃR�s�[����
      /tmp/vinegar/example.feature �� UTF-8 �ɂ���
      """
    Then �e�V�i���I�ƃX�e�b�v���\������Ă��邱��
      # �V�i���I�͈ȉ��̂S��
      # 1. �R�}���h�����s���ē����̌��ʕ񍐏��𐶐�����
      # 2. �I�v�V�����Ŏw�肵���f�B���N�g���Ƀt�@�C�����o�͂���
      # 3. ���݂��Ȃ��t�@�C�����w�肷��ƃG���[��\������
      # 4. �R�����g�͔��l�ɃR�s�[����
      # ���u�X�e�b�v�����݂��Ȃ��V�i���I�͖�������v�͕\������Ȃ�����
      # �e�X�e�b�v�ɂ��Ă͌��̃t�@�C���Ɣ�r���ĕs�����Ȃ����m�F���邱��

  Scenario: �I�v�V�����Ŏw�肵���f�B���N�g���Ƀt�@�C�����o�͂���
    Given �z�[���f�B���N�g���Ɉړ�����
    When "vinegar -o ~/Desktop /tmp/vinegar/example.feature" �R�}���h�����s����
    Then ~/Desktop �� example.xlsx �t�@�C�������݂��邱��
    And  /tmp/vinegar �� example.xlsx �t�@�C�������݂��Ȃ�����
    And  �J�����g�f�B���N�g���� example.xlsx �t�@�C�������݂��Ȃ�����

    Given �z�[���f�B���N�g���Ɉړ�����
    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" �R�}���h�����s����
      # /tmp/vinegar/path/to/deep/dir �͑��݂��Ȃ�����
      # �K�v�ł���� rm -rf /tmp/vinegar/path/ �ŏ����Ă���
    Then �ȉ��̃G���[���\������邱��:
      """
      /tmp/vinegar/path/to/deep/dir: No such file or directory
      """
      # �G���[���b�Z�[�W�����{��̉\��������
    And  /tmp/vinegar/path/to/deep/dir �� example.xlsx �t�@�C�������݂��Ȃ�����

    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f" �R�}���h�����s����
    Then �G���[���\�����ꂸ�I�����邱��
    And  /tmp/vinegar/path/to/deep/dir �� example.xlsx �t�@�C�������݂��邱��

  Scenario: ���݂��Ȃ��t�@�C�����w�肷��ƃG���[��\������
    When "vinegar /path/to/dummy.feature" �R�}���h�����s����
    Then �ȉ��̃G���[���\������邱��:
      """
      /path/to/dummy.feature: No such file or directory
      """

  Scenario: And �� But ���������p�[�X����Ă��邱��
    When �z�[���f�B���N�g���Ɉړ�����
    And  "vinegar -o ~/Desktop /tmp/vinegar/example.feature" �R�}���h�����s����
    And  ���̃e�L�X�g�͏�L�Q�X�e�b�v�ƍ��킹�āuWhen�v�ɋL�q����Ă��邱��
    Then 1. ���̃e�L�X�g�́uThen�v�ɋL�q����Ă��邱��
    And  2. ���̃e�L�X�g�� 1. �̎��s�́uThen�v�ɋL�q����Ă��邱��
    But  3. ���̃e�L�X�g�� 3. �̎��s�́uThen�v�ɋL�q����Ă��邱��
    Given ���̃e�L�X�g�͎��̍s�́uGiven�v
    Then 1. ���̃e�L�X�g�́uThen�v�ɋL�q����Ă��邱��
    When �z�[���f�B���N�g���Ɉړ�����
    Then 1. ���̃e�L�X�g�́uThen�v�ɋL�q����Ă��邱��
    When �z�[���f�B���N�g���Ɉړ�����
    Then 1. ���̃e�L�X�g�́uThen�v�ɋL�q����Ă��邱��

  @pending
  Scenario: pending �^�O���t���Ă���V�i���I�͖�������

  Scenario: �R�����g�͔��l�ɃR�s�[����
    When "vinegar /tmp/vinegar/example.feature" �����s����
    Then �u�e�V�i���I�ƃX�e�b�v���\������Ă��邱�Ɓv�ɔ��l���ݒ肳��Ă��邱��
    Then �u"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" �R�}���h�����s����v�ɔ��l���ݒ肳��Ă��邱��
    Then ���̃e�X�g�̔��l�Ɉȉ����L������Ă��邱��
      #�����̓t�@�C�������̃R�����g�ł�
      #���l�ɓ����Ă��܂����H
