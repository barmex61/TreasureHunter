<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="attackObjects" tilewidth="72" tileheight="40" tilecount="30" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_1_01.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="50" y="34">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 -17,-2 -20,-5 -18,-12 -30,-12 -32,-4 -22,4 -11,4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="1">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_1_02.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="31" y="25">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-13,0 -12,-3 1,-3 1,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="2">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_1_03.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="31" y="22">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 -13,0 -13,3 -11,5 0,5"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="3">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_2_01.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="26" y="37">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 11,-3 18,-10 18,-17 29,-17 31,-7 22,1 9,3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="4">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_2_02.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="44" y="22">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 13,0 11,-3 0,-3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="5">
  <properties>
   <property name="attackType" propertytype="AttackType" value="AIR_ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/air_attack_2_03.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="44" y="23">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,1 12,1 13,-2 13,-4 0,-4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="6">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/attack_1_01.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="42" y="28">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,1 7,1 9,-5 0,-5"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="7">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/attack_1_02.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="51" y="21">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 -6,9 3,9 10,6 13,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="8">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_1"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/attack_1_03.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="47" y="22">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 0,5 11,5 14,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="9">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/attack_2_01.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="40" y="14">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,1 0,-12 3,-12 5,-10 5,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="10">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/attack_2_02.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="47" y="31">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 4,-3 5,-10 4,-18 9,-13 13,-3 12,5 6,8"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="11">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_2"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/attack_2_03.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="45" y="28">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,1 11,1 13,-2 13,-4 0,-4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="12">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_3"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/attack_3_01.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="20" y="28">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 10,0 10,-5 -2,-5 -2,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="13">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_3"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/attack_3_02.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="47" y="10">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 5,-8 12,-8 13,-3 13,4 6,14 -4,18 4,6"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="14">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK_3"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/attack_3_03.png" width="64" height="40"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="45" y="21">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,1 11,1 13,-1 13,-4 0,-4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="15">
  <properties>
   <property name="attackType" propertytype="AttackType" value="PINK_STAR_ATTACK"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/pink_star_attack_01.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="17" y="6">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-2,0 -10,7 -10,19 -2,23 9,21 12,17 11,5 5,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="16">
  <properties>
   <property name="attackType" propertytype="AttackType" value="PINK_STAR_ATTACK"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/pink_star_attack_02.png" width="34" height="30"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="18" y="5">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-6,2 -10,7 -10,19 -2,24 6,23 12,17 11,5 7,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="17">
  <properties>
   <property name="attackType" propertytype="AttackType" value="PINK_STAR_ATTACK"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/pink_star_attack_03.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="17" y="6">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-2,0 -10,7 -10,19 -5,23 9,21 12,17 11,5 5,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="18">
  <properties>
   <property name="attackType" propertytype="AttackType" value="PINK_STAR_ATTACK"/>
   <property name="keyFrameIx" type="int" value="3"/>
  </properties>
  <image source="../graphics/attackObjects/pink_star_attack_04.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="18" y="6">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-7,2 -10,7 -10,20 1,23 12,18 12,7 9,0 0,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="19">
  <properties>
   <property name="attackType" propertytype="AttackType" value="THROW"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/sword.png" width="20" height="20"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="7" y="12">
    <properties>
     <property name="userData" value="rangeAttackFixture"/>
    </properties>
    <polygon points="0,1 11,1 13,-1 13,-4 0,-4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="20">
  <properties>
   <property name="attackType" propertytype="AttackType" value="CRABBY_ATTACK"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/crabby_attack_01.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="24" y="15">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="1,0 -15,0 -20,4 -20,9 -14,12 1,12 1,7"/>
   </object>
   <object id="4" x="49" y="15">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 0,12 18,12 23,9 22,2 17,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="21">
  <properties>
   <property name="attackType" propertytype="AttackType" value="CRABBY_ATTACK"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/crabby_attack_02.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="26" y="20">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 -9,-1 -15,-3 -18,1 -15,5 -9,4 0,3"/>
   </object>
   <object id="4" x="48" y="19">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 11,0 16,-2 20,3 16,7 11,4 -1,4"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="22">
  <properties>
   <property name="attackType" propertytype="AttackType" value="CRABBY_ATTACK"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/crabby_attack_03.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="2" x="47" y="24">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="6,1 6,-12 -1,-12 -1,1"/>
   </object>
   <object id="5" x="21" y="24">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="6,1 6,-12 -1,-12 -1,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="23">
  <properties>
   <property name="attackType" propertytype="AttackType" value="CRABBY_ATTACK"/>
   <property name="keyFrameIx" type="int" value="3"/>
  </properties>
  <image source="../graphics/attackObjects/crabby_attack_04.png" width="72" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="26" y="22">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 -3,0 -9,-7 -7,-12 0,-12 2,-10"/>
   </object>
   <object id="4" x="55" y="21">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="-2,-1 -7,0 -9,-7 -7,-12 0,-12 2,-8"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="24">
  <properties>
   <property name="attackType" propertytype="AttackType" value="FIERCE_TOOTH_ATTACK"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/fierce_tooth_attack_01.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="13" y="19">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,0 5,-3 5,-11 1,-14 -12,-14 -16,-10 -16,-2 -10,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="25">
  <properties>
   <property name="attackType" propertytype="AttackType" value="FIERCE_TOOTH_ATTACK"/>
   <property name="keyFrameIx" type="int" value="1"/>
  </properties>
  <image source="../graphics/attackObjects/fierce_tooth_attack_02.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="16" y="19">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,-2 5,-3 5,-11 1,-13 -10,-13 -14,-10 -14,-4 -9,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="26">
  <properties>
   <property name="attackType" propertytype="AttackType" value="FIERCE_TOOTH_ATTACK"/>
   <property name="keyFrameIx" type="int" value="2"/>
  </properties>
  <image source="../graphics/attackObjects/fierce_tooth_attack_03.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="18" y="21.5372">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,-1.53719 3.68421,-2.30579 3.68421,-8.45455 0.736842,-9.99174 -7.36842,-9.99174 -10.3158,-7.68595 -10.3158,-3.07438 -6.63158,-1.53719"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="27">
  <properties>
   <property name="attackType" propertytype="AttackType" value="FIERCE_TOOTH_ATTACK"/>
   <property name="keyFrameIx" type="int" value="3"/>
  </properties>
  <image source="../graphics/attackObjects/fierce_tooth_attack_04.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="19.4571" y="23.2893">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,-1.28926 4.54294,-1.93388 4.54294,-7.09091 0.908587,-8.38017 -9.08587,-8.38017 -12.7202,-6.44628 -12.7202,-2.57851 -8.17729,-1.28926"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="28">
  <properties>
   <property name="attackType" propertytype="AttackType" value="FIERCE_TOOTH_ATTACK"/>
   <property name="keyFrameIx" type="int" value="4"/>
  </properties>
  <image source="../graphics/attackObjects/fierce_tooth_attack_05.png" width="34" height="30"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="19" y="22">
    <properties>
     <property name="userData" value="meleeAttackFixture"/>
    </properties>
    <polygon points="0,-1.27273 3.67036,-1.90909 3.67036,-7 0.734072,-8.27273 -7.34072,-8.27273 -10.277,-6.36364 -10.277,-2.54545 -6.60665,-1.27273"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="29">
  <properties>
   <property name="attackType" propertytype="AttackType" value="ATTACK"/>
   <property name="keyFrameIx" type="int" value="0"/>
  </properties>
  <image source="../graphics/attackObjects/wood_spike.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="13" y="5">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="rangeAttackFixture"/>
    </properties>
    <polygon points="0,0 -8,0 -10,2 -10,4 -8,6 0,6"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
