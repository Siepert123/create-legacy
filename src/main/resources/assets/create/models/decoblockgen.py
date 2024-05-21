
def getItemModel(parent):
    return '{\n  "parent": "' + parent + '"\n}'

def getBlockModel(texture):
    return '{\n  "parent": "block/cube_all",\n  "textures": {\n    "all": "' + texture + '"\n  }\n}'

pathItem = "item/stone/"
pathBlock = "block/stone/"

stoneCutPrefix = "stone_cut_"
stoneBricksPrefix = "stone_bricks_"
stoneBricksFancyPrefix = "stone_bricks_fancy_"

stoneTypes = ["tuff", "calcite", "asurine", "crimsite", "tuff", "veridium",
    "deepslate", "dripstone", "limestone", "ochrum", "scorchia", "scoria",
              "andesite", "diorite", "granite"]

for stone in stoneTypes:
    print("Creating cut " + stone + " models")
    fileBlock = open(pathBlock + stoneCutPrefix + stone + ".json", "w")
    fileItem = open(pathItem + stoneCutPrefix + stone + ".json", "w")

    fileBlock.write(getBlockModel("create:block/palettes/stone_types/cut/" + stone))
    fileItem.write(getItemModel("create:" + pathBlock + stoneCutPrefix + stone))

    fileBlock.close()
    fileItem.close()
    
for stone in stoneTypes:
    print("Creating " + stone + " bricks models")
    fileBlock = open(pathBlock + stoneBricksPrefix + stone + ".json", "w")
    fileItem = open(pathItem + stoneBricksPrefix + stone + ".json", "w")

    fileBlock.write(getBlockModel("create:block/palettes/stone_types/bricks/" + stone))
    fileItem.write(getItemModel("create:" + pathBlock + stoneBricksPrefix + stone))

    fileBlock.close()
    fileItem.close()
    
for stone in stoneTypes:
    print("Creating fancy " + stone + " bricks models")
    fileBlock = open(pathBlock + stoneBricksFancyPrefix + stone + ".json", "w")
    fileItem = open(pathItem + stoneBricksFancyPrefix + stone + ".json", "w")

    fileBlock.write(getBlockModel("create:block/palettes/stone_types/bricks_fancy/" + stone))
    fileItem.write(getItemModel("create:" + pathBlock + stoneBricksFancyPrefix + stone))

    fileBlock.close()
    fileItem.close()
