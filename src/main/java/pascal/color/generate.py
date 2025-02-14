import json, sys

with open("colors.json", "r") as f:
    data = json.load(f)

with open("Colors.java", "w") as sys.stdout:
    print("package pascal.color;")
    print("/** Color my pencils. */")
    print("public final class Colors {")
    for color, shade in data.items():
        color_upper = color[0].upper() + color[1:]
        print(f"/** Shades of {color} */")
        print(f"public final class {color_upper} {{")
        for num, hc in shade.items():
            print(f"/** {color}-{num}. */")
            print(f"public static final String z{num}()", "{ return ", f'"{hc}";', "}")
        print("}")
    print("}")
