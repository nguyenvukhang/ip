import json, sys

with open("colors.json", "r") as f:
    data = json.load(f)

with open("Colors.java", "w") as sys.stdout:
    print("package pascal.color;")
    print("public final class Colors {")
    for key, shade in data.items():
        key = key[0].upper() + key[1:]
        print(f"public final class {key} {{")
        for num, hc in shade.items():
            print(f"public final static String z{num}()", "{ return ", f'"{hc}";', "}")
        print("}")
    print("}")
