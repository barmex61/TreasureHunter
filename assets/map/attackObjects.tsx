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
    <polygon points="0,0 -12,-1 -17,-2 -20,-5 -18,-9 -18,-12 -30,-12 -32,-10 -32,-4 -30,-2 -23,3 -20,4 -11,4 -2,2"/>
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
    <polygon points="0,0 -13,0 -13,-2 -12,-3 1,-3 1,0"/>
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
    <polygon points="0,0 4,-1 9,-2 15,-6 18,-11 18,-17 29,-17 31,-15 31,-9 29,-4 22,1 15,3 7,3 0,1"/>
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
    <polygon points="0,0 13,0 13,-2 11,-3 0,-3"/>
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
    <polygon points="0,0 1,1 12,1 13,-2 13,-4 1,-4 1,-2 0,-2"/>
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
   <object id="1" x="43" y="29">
    <polygon points="0,0 6,0 8,-2 8,-6 0,-6 0,-4 -1,-4 -1,-2 0,-2"/>
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
   <object id="1" x="50" y="25">
    <polygon points="0,0 2,0 0,1 -5,4 -5,5 3,5 11,2 14,-1 14,-4 1,-4 1,-2 0,-2"/>
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
   <object id="1" x="47" y="26">
    <polygon points="0,0 1,0 1,1 11,1 13,-1 13,-4 1,-4 1,-2 0,-2"/>
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
    <polygon points="0,0 0,-12 3,-12 5,-10 5,0 3,1 2,1"/>
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
    <polygon points="0,0 4,-3 5,-10 6,-15 4,-17 4,-18 6,-18 13,-3 13,4 10,8 6,8"/>
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
    <polygon points="0,0 1,1 11,1 13,-2 13,-4 1,-4 1,-2 0,-2"/>
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
    <polygon points="0,0 9,0 11,-2 11,-4 10,-5 -2,-5 -2,-2"/>
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
    <polygon points="0,0 6,-8 10,-8 13,-3 13,4 6,14 -2,18 -4,18 -4,17 3,7 4,2"/>
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
    <polygon points="0,0 1,1 11,1 13,-1 13,-4 1,-4 1,-3 0,-3"/>
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
   <object id="1" x="14" y="23">
    <polygon points="0,0 -3,1 -6,-1 -6,3 -2,6 4,6 8,0 10,0 8,3 8,4 11,4 15,0 15,-4 11,-7 11,-8 13,-8 15,-6 15,-11 13,-14 9,-14 9,-13 5,-13 5,-14 8,-16 7,-17 1,-17 -2,-13 -2,-8 -3,-8 -4,-12 -6,-12 -7,-9 -7,-6 -6,-4"/>
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
   <object id="2" x="24" y="13">
    <polygon points="0,0 0,-2 4,0 5,0 5,-3 1,-7 -5,-7 -9,-2 -11,-1 -11,-3 -9,-5 -12,-5 -16,-1 -16,3 -12,5 -12,7 -15,5 -16,5 -16,10 -14,13 -10,13 -10,12 -4,12 -9,14 -8,16 -2,16 1,12 1,6 3,11 6,8 6,5 5,3"/>
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
   <object id="1" x="25" y="17">
    <polygon points="0,0 0,-2 3,0 4,0 4,-5 2,-8 -2,-8 -6,-7 -6,-8 -3,-10 -4,-11 -10,-11 -13,-7 -13,-2 -15,-6 -16,-6 -18,-3 -18,0 -14,5 -12,5 -12,7 -14,7 -16,5 -17,5 -17,8 -15,11 -13,11 -13,12 -8,12 -1,5 -1,8 -3,9 -3,10 0,10 4,6 4,2"/>
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
   <object id="1" x="14" y="12">
    <polygon points="0,0 6,-6 11,-6 15,-2 15,1 14,1 12,-1 9,-1 10,1 16,6 16,9 14,12 13,12 11,8 11,13 9,16 6,17 2,17 1,15 4,13 0,13 0,14 -4,14 -6,11 -6,6 -3,9 -1,6 -6,4 -6,1 -4,-3 -2,-4 1,-4 1,-3 -1,-2"/>
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
    <polygon points="0,0 1,0 1,1 11,1 13,-1 13,-4 1,-4 1,-3 0,-3"/>
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
   <object id="1" x="25" y="20">
    <polygon points="0,0 -12,0 -11,-2 -3,-3 -1,-5 -15,-5 -21,-1 -21,4 -16,7 -7,7 -5,5 -11,5 -13,4 -12,3 0,3"/>
   </object>
   <object id="2" x="49" y="23">
    <polygon points="0,0 14,0 14,1 4,1 4,2 10,4 18,4 23,1 23,-4 17,-8 8,-8 6,-7 6,-6 11,-6 13,-4 14,-4 14,-3 -3,-3 -3,-1"/>
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
   <object id="1" x="26" y="23">
    <polygon points="0,0 -2,0 -2,1 -7,1 -7,-1 -9,-1 -11,3 -14,3 -18,0 -18,-3 -14,-6 -11,-6 -9,-3 -7,-3 -7,-2 -3,-2 -2,-3 0,-3"/>
   </object>
   <object id="2" x="48" y="23">
    <polygon points="0,0 2,0 2,-1 6,-1 7,0 11,0 11,1 14,3 16,3 20,0 20,-3 16,-6 13,-6 11,-4 11,-3 7,-3 7,-4 2,-4 2,-3 -3,-3 -3,-2 -3,-1"/>
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
    <polygon points="0,0 2,1 4,1 6,-1 6,-3 2,-3 2,-8 6,-8 6,-10 4,-12 1,-12 -1,-10 -1,0"/>
   </object>
   <object id="3" x="27" y="24">
    <polygon points="1,0 -1,1 -4,1 -6,-1 -6,-3 -3,-3 -2,-3 -2,-8 -6,-8 -6,-10 -4,-12 -1,-12 1,-9"/>
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
   <object id="1" x="26" y="22">
    <polygon points="0,0 -3,0 -5,-2 -5,-5 -7,-5 -9,-6 -9,-10 -7,-12 -5,-12 -5,-8 -2,-8 -2,-12 0,-12 2,-10 2,-6 0,-4 -2,-4 -2,-2 0,-2"/>
   </object>
   <object id="2" x="48" y="21">
    <polygon points="0,0 3,0 5,-2 5,-4 7,-4 9,-6 9,-10 7,-12 5,-12 5,-8 2,-8 2,-12 0,-12 -2,-10 -2,-6 1,-4 2,-4 2,-3 -3,-3 -3,0"/>
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
    <polygon points="0,0 5,-3 5,-5 5,-11 1,-14 -12,-14 -16,-10 -16,-2 -10,0"/>
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
    <polygon points="0,-2 5,-3 5,-5 5,-11 1,-13 -10,-13 -14,-10 -14,-4 -9,-2"/>
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
    <polygon points="0,-1.53719 3.68421,-2.30579 3.68421,-3.84298 3.68421,-8.45455 0.736842,-9.99174 -7.36842,-9.99174 -10.3158,-7.68595 -10.3158,-3.07438 -6.63158,-1.53719"/>
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
    <polygon points="0,-1.28926 4.54294,-1.93388 4.54294,-3.22314 4.54294,-7.09091 0.908587,-8.38017 -9.08587,-8.38017 -12.7202,-6.44628 -12.7202,-2.57851 -8.17729,-1.28926"/>
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
    <polygon points="0,-1.27273 3.67036,-1.90909 3.67036,-3.18182 3.67036,-7 0.734072,-8.27273 -7.34072,-8.27273 -10.277,-6.36364 -10.277,-2.54545 -6.60665,-1.27273"/>
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
     <property name="userData" value="rangeAttackFixture"/>
    </properties>
    <polygon points="0,0 -8,0 -10,2 -10,4 -8,6 0,6"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
