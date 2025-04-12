<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="72" tileheight="93" tilecount="23" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN_SWORD"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="item" propertytype="Item" value="NONE"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image width="64" height="40" source="../graphics/captain_clown_sword.png"/>
  <objectgroup draworder="index" id="3">
   <object id="3" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="12" x="12" y="30">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="15" x="26" y="19">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,11 11,11 11,0"/>
   </object>
   <object id="16" x="21" y="12" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="16">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_LEFT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="51" height="53" source="../graphics/back_palm_tree_left.png"/>
 </tile>
 <tile id="19">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="64" source="../graphics/back_palm_tree_regular.png"/>
 </tile>
 <tile id="22">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="BACKGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="BACK_PALM_TREE_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="52" height="53" source="../graphics/back_palm_tree_right.png"/>
 </tile>
 <tile id="25">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FRONT_PALM_TREE"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="39" height="32" source="../graphics/front_palm_tree.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="4" y="3" width="32" height="5">
    <properties>
     <property name="userData" value="platform"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="28">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_LEFT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="32" source="../graphics/palm_tree_bottom_left.png"/>
 </tile>
 <tile id="27">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_RIGHT"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="30" source="../graphics/palm_tree_bottom_right.png"/>
 </tile>
 <tile id="26">
  <properties>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="PALM_TREE_BOTTOM_REGULAR"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/palm_tree_bottom_regular.png"/>
 </tile>
 <tile id="29">
  <properties>
   <property name="animFrameDuration" type="float" value="0.2"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="CHEST"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="64" height="35" source="../graphics/chest.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="19" y="11" width="28" height="23">
    <properties>
     <property name="userData" value="chest"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="30">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND"/>
   <property name="gameObject" propertytype="GameObject" value="FLAG"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="34" height="93" source="../graphics/flag.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="4" height="93">
    <properties>
     <property name="userData" value="flag"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="31">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SHIP_HELM"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="31" height="32" source="../graphics/ship_helm.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="4" y="4" width="23" height="23">
    <properties>
     <property name="userData" value="shipHelm"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="32">
  <properties>
   <property name="damage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="SPIKES"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/spikes.png"/>
  <objectgroup draworder="index" id="2">
   <object id="9" x="8" y="16" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="12" x="16" y="19" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="13" x="24" y="17" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="0.4"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
   <object id="14" x="0" y="21" width="7" height="9" rotation="1.4409">
    <properties>
     <property name="restitution" type="float" value="1"/>
     <property name="userData" value="spike"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="34">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="SWORD"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
  </properties>
  <image width="20" height="20" source="../graphics/sword.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="18" y="13">
    <polygon points="0,0 2,-2 2,-5 -10,-5 -12,-6 -14,-6 -14,-4 -16,-4 -16,-3 -17,-3 -17,0 -14,0 -14,1 -12,1 -12,0 -11,0 -11,-1 -10,-1 -10,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="35">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER"/>
   <property name="gameObject" propertytype="GameObject" value="CAPTAIN_CLOWN"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="item" propertytype="Item" value="NONE"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="float" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image width="64" height="40" source="../graphics/captain_clown.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="24" y="17" width="15" height="15">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="12" y="30">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="18,0 21,0 21,-1 18,-1"/>
   </object>
   <object id="3" x="26" y="19">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,11 11,11 11,0"/>
   </object>
   <object id="4" x="21" y="12" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="36">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="SWORD_EMBEDDED"/>
  </properties>
  <image width="20" height="20" source="../graphics/sword_embedded.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="14">
    <polygon points="0,0 3,0 3,2 5,2 5,1 6,1 6,0 7,0 7,1 14,1 14,-4 7,-4 7,-3 6,-3 6,-4 5,-4 5,-5 3,-5 3,-3 1,-3 1,-2 0,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="37">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="AIR_ATTACK_1"/>
  </properties>
  <image width="22" height="26" source="../graphics/air_attack_1.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="17">
    <polygon points="0,0 7,0 19,-8 21,-17 22,-17 21,-6 14,3 11,4 6,4 0,1"/>
   </object>
   <object id="2" x="9" y="23">
    <polygon points="0,0 6,0 11,-6 12,-6 10,0 6,3 3,3 0,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="39">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="AIR_ATTACK_2"/>
  </properties>
  <image width="31" height="23" source="../graphics/air_attack_2.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="19">
    <polygon points="0,0 8,0 21,-7 24,-19 25,-19 27,-13 27,-8 22,0 15,2 6,2 6,1 0,1"/>
   </object>
   <object id="2" x="20" y="22">
    <polygon points="0,0 3,0 8,-7 9,-10 10,-10 11,-8 11,-4 10,-2 7,0 5,1 0,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="41">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="ATTACK_1"/>
  </properties>
  <image width="28" height="17" source="../graphics/attack_1.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="16">
    <polygon points="0,0 3,-1 5,-3 5,-6 3,-8 2,-8 2,-7 3,-7 3,-3 2,-3 2,-2 1,-2 1,-1 0,-1"/>
   </object>
   <object id="2" x="11" y="17">
    <polygon points="0,0 0,-1 7,-8 7,-9 8,-9 8,-11 7,-11 7,-13 3,-16 3,-17 5,-17 11,-14 14,-11 14,-10 12,-8 6,-2 4,-2 2,0"/>
   </object>
   <object id="3" x="22" y="11">
    <polygon points="0,0 3,-3 4,-3 4,-5 2,-6 2,-7 4,-7 6,-5 6,-3 5,-3 4,-1 3,0 1,0 1,1 0,1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="44">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="ATTACK_2"/>
  </properties>
  <image width="38" height="37" source="../graphics/attack_2.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="3" y="10">
    <polygon points="0,0 2,-1 2,-6 1,-8 -2,-10 -3,-10 -3,-9 -1,-6 0,-6"/>
   </object>
   <object id="3" x="7" y="5">
    <polygon points="0,0 8,5 12,10 16,18 17,18 17,28 14,31 14,32 15,32 20,28 22,24 22,16 19,9 13,3 6,0 1,-1 -1,-1"/>
   </object>
   <object id="5" x="33" y="18">
    <polygon points="0,0 2,5 3,5 3,13 2,13 2,15 3,15 5,12 5,5 1,-1 -1,-1"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="47">
  <properties>
   <property name="gameObject" propertytype="GameObject" value="ATTACK_3"/>
  </properties>
  <image width="42" height="36" source="../graphics/attack_3.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="35">
    <polygon points="0,0 3,-4 4,-4 4,-9 3,-9 3,-10 6,-9 6,-4 3,0 1,1 0,1"/>
   </object>
   <object id="2" x="14" y="31">
    <polygon points="0,0 4,-2 9,-7 12,-14 13,-14 13,-22 7,-30 7,-31 8,-31 17,-23 19,-18 19,-11 18,-8 11,-2 6,0 2,1 0,1"/>
   </object>
   <object id="3" x="37" y="21">
    <polygon points="0,0 1,-3 2,-3 2,-11 -3,-18 -3,-19 -2,-19 4,-12 5,-10 5,-5 4,-3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="48">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="CRABBY"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2.5"/>
  </properties>
  <image width="72" height="32" source="../graphics/crabby.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="27" y="13" width="20" height="16">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="29" y="15">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,13 16,13 16,0"/>
   </object>
   <object id="4" x="11" y="28">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="24,0 28,0 28,-1 24,-1"/>
   </object>
   <object id="5" x="26" y="8" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="49">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="FIERCE_TOOTH"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image width="34" height="30" source="../graphics/fierce_tooth.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="9" y="7" width="17" height="19">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="11" y="9">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,16 13,16 13,0"/>
   </object>
   <object id="3" x="-8" y="25">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="24,0 28,0 28,-1 24,-1"/>
   </object>
   <object id="4" x="6" y="5" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="50">
  <properties>
   <property name="aiTreePath" value="ai/crew.tree"/>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="gameObject" propertytype="GameObject" value="PINK_STAR"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="isFlipped" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2"/>
   <property name="life" type="int" value="3"/>
   <property name="speed" type="int" value="2"/>
   <property name="startAnimType" propertytype="StartAnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2"/>
  </properties>
  <image width="34" height="30" source="../graphics/pink_star.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="10" y="10" width="14" height="17">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" x="11" y="12">
    <properties>
     <property name="userData" value="bodyFixture"/>
    </properties>
    <polygon points="0,0 0,14 12,14 12,0"/>
   </object>
   <object id="3" x="-8" y="26">
    <properties>
     <property name="userData" value="footFixture"/>
    </properties>
    <polygon points="24,0 28,0 28,-1 24,-1"/>
   </object>
   <object id="4" x="6" y="7" width="22" height="22">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="radius" type="float" value="4"/>
     <property name="userData" value="sensorFixture"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
