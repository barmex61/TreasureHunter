<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="attackObjects" tilewidth="64" tileheight="40" tilecount="20" columns="0">
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
</tileset>
