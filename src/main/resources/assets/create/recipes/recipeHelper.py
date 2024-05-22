import json

def generate2x2CompactJson(inputItem, inputItemMeta, resultItem, resultItemMeta, count, group):
    thing = json.dumps({
        "type": "minecraft:crafting_shaped",
        "group": group,
        "pattern": ["##", "##"],
        "key": {
            "#": {"item": inputItem, "data": inputItemMeta},
            },
        "result": {
            "item": resultItem,
            "data": resultItemMeta,
            "count": count
            }
        }, indent=2)
    return thing

def generate2x1CompactJson(inputItem, inputItemMeta, resultItem, resultItemMeta, count, group):
    thing = json.dumps({
        "type": "minecraft:crafting_shaped",
        "group": group,
        "pattern": ["#", "#"],
        "key": {
            "#": {"item": inputItem, "data": inputItemMeta},
            },
        "result": {
            "item": resultItem,
            "data": resultItemMeta,
            "count": count
            }
        }, indent=2)
    return thing

def generateSingletonJson(inputItem, inputItemMeta, resultItem, resultItemMeta, count, group):
    thing = json.dumps({
        "type": "minecraft:crafting_shaped",
        "group": group,
        "pattern": ["#"],
        "key": {
            "#": {"item": inputItem, "data": inputItemMeta},
            },
        "result": {
            "item": resultItem,
            "data": resultItemMeta,
            "count": count
            }
        }, indent=2)
    return thing

stoneTypes = ["asurine", "calcite", "crimsite", "deepslate", "dripstone", "limestone", "ochrum", "scorchia", "scoria", "tuff", "veridium", "andesite", "diorite", "granite"]
otherStoneTypes = ["calcite", "tuff", "asurine", "crimsite", "limestone", "ochrum", "scorchia", "scoria", "veridium"]
def getItemInList(list0, search):
    for index in range(len(list0)):
        if list0[index] == search:
            return index
    return None

#folder: prefix
decorationTypes = {"bricks": "stone_bricks",
                   "bricks/fancy": "stone_bricks_fancy",
                   "cut": "stone_cut",
                   "layered": "stone_layered",
                   "pillar": "stone_pillar",
                   "polished": "stone_polished"}

for decoType in decorationTypes:
    if decoType == "bricks":
        for stone in stoneTypes:
            file = open("decoration/bricks/" + stone + ".json", "w")
            stoneIndex = getItemInList(stoneTypes, stone)
            file.write(generate2x2CompactJson("create:stone_polished", stoneIndex,
                                              "create:stone_bricks", stoneIndex, 4, "bricks"))
            print("decoration/bricks/" + stone + ".json")
            file.close()
            file = open("decoration/bricks/" + stone + "_from_fancy.json", "w")
            stoneIndex = getItemInList(stoneTypes, stone)
            file.write(generate2x2CompactJson("create:stone_bricks_fancy", stoneIndex,
                                              "create:stone_bricks", stoneIndex, 4, "bricks"))
            print("decoration/bricks/" + stone + "_from_fancy.json")
            file.close()
    elif decoType == "bricks/fancy":
        for stone in stoneTypes:
            file = open("decoration/bricks_fancy/" + stone + ".json", "w")
            stoneIndex = getItemInList(stoneTypes, stone)
            file.write(generate2x2CompactJson("create:stone_bricks", stoneIndex,
                                              "create:stone_bricks_fancy", stoneIndex, 4, "bricks_fancy"))
            print("decoration/bricks_fancy/" + stone + ".json")
            file.close()
    elif decoType == "polished":
        for stone in stoneTypes:
            stoneIndex = getItemInList(stoneTypes, stone)
            otherStoneIndex = getItemInList(otherStoneTypes, stone)
            if (otherStoneIndex != None):
                file = open("decoration/polished/" + stone + ".json", "w")
                file.write(generate2x2CompactJson("create:stone", otherStoneIndex,
                                                  "create:stone_polished", stoneIndex, 4, "polished"))
                print("decoration/polished/" + stone + ".json")
                file.close()
            file = open("decoration/polished/" + stone + "_from_cut.json", "w")
            file.write(generate2x2CompactJson("create:stone_cut", stoneIndex,
                                              "create:stone_polished", stoneIndex, 4, "polished"))
            file.close()
    elif decoType == "cut":
        for stone in stoneTypes:
            file = open("decoration/cut/" + stone + ".json", "w")
            stoneIndex = getItemInList(stoneTypes, stone)
            file.write(generate2x2CompactJson("create:stone_polished", stoneIndex,
                                              "create:stone_cut", stoneIndex, 4, "bricks_fancy"))
            print("decoration/cut/" + stone + ".json")
            file.close()
    elif decoType == "pillar":
        for stone in stoneTypes:
            file = open("decoration/pillar/" + stone + ".json", "w")
            stoneIndex = getItemInList(stoneTypes, stone)
            file.write(generate2x1CompactJson("create:stone_polished", stoneIndex,
                                              "create:stone_pillar", stoneIndex, 2, "pillar"))
            print("decoration/pillar/" + stone + ".json")
            file.close()

