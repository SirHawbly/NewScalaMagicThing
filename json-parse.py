import json
import sys
import os
# import re
# import sqlite3


def json_parse(file_name):

    # var for all headers, and save and db files
    # lo = []
    out_path = "parsed_cards.csv"
    # db_path = "temp.sqlite"
    count = 0

    # open up a db connection
    # db = sqlite3.connect(db_path)
    # c = db.cursor()

    # keys in the json we care about
    key_list = ['oracle_id', 'name', 'released_at', 'scryfall_uri', 'layout', 'mana_cost',
                'type_line', 'oracle_text', 'loyalty', 'colors', 'color_identity', 'set',
                'set_name']

    # headers for the SQL db, set is protected...
    # header_list = ['oracle_id', 'name', 'released_at', 'scryfall_uri', 'layout', 'mana_cost', 'cmc',
                  # 'type_line', 'oracle_text', 'loyalty', 'colors', 'color_identity', 'set_id', 'set_name']

    # create the db string for the db this will be stored in
    # create_table = "create table Cards ("
    # for k in header_list:
        # if header_list.index(k) == 0 :
            # create_table += "'{}' text primary key".format(k)
        # else:
            # create_table += ", '{}' text".format(k)
    # create_table += ')'
    # print(create_table)

    # create the table
    # c.execute(create_table)

    # remove our file if it exists
    if os.path.exists(out_path):
        os.remove(out_path)

    print('Loading', file_name, '...', end='')

    # open the json and csv files
    with open(out_path, "w+") as outfile:
        with open(file_name, 'r') as json_file:

            # write out the header for the csv file
            outfile.write(str(key_list)[1:-1].replace(" ", "") + "\n")

            # load the data from the input file
            json_data = json.load(json_file)

            print('Loaded.')

            for json_obj in json_data:

                data = {}

                # store all of the different keys that come up
                # for key in json_obj:
                    # if key not in lo:
                        # lo += [key]

                print(json_obj['name'] + ': ', end='')


                # if the obj is not english or a reprint, skip it
                if json_obj['lang'] != 'en' or json_obj['reprint']:
                    print('reprint or non-english')
                    continue

                print('parsing ...', end='')
                count += 1

                # pull out these fields from the json obj
                # for i in key_list:
                    # if i in json_obj:
                        # if json_obj[i] == "" and False:
                            # data[i] = "[]"

                        # elif 'color' in i and False:
                            # data[i] = str(json_obj[i]).replace(", ", ":")[1:-1]
                        # else:
                            # data[i] = json_obj[i]

                for i in key_list:
                    if i not in json_obj:
                        continue
                    else:
                        if type(json_obj[i]) == type(list()):
                            temp = ''
                            for j in json_obj[i]:
                                temp += str(j) + ' '
                            data[i] = temp
                        else:
                            data[i] = json_obj[i]

                        # if type(data[i]) not in lo:
                            # lo += [type(data[i])]

                # data.replace("", "")

                line = 'oracle_id' + '===' + data['oracle_id']
                for k in data:
                    if k == 'oracle_id': continue
                    if data[k] != '':
                        line += ',,, ' + str(k) + '===' + str(data[k])
                    else:
                        line += ',,, ' + str(k) + '===' + '---'

                # print(json_obj['name'])
                outfile.write(line.replace("\n", "|||") + "\n")
                print(' done.')
                # return

                # line = str(data).replace("\': \'", "===").replace("': ['", "'===['").replace("'cmc': ", "'cmc'===")
                # line = line.replace("', '", "',,,'").replace("], '", "],,,'").replace("0, '", "0,,,'")
                # line = line.replace("', \"", "',,,'")

                # write out the dict, minus the '{' and '},' at both ends
                # -- outfile.write(line[1:-1] + "\n")

                # cols = "'" + str(header_list[0]) + "'"',,,'
                # for h in header_list[1:]:
                    # cols += ", '{}'".format(h)

                # create the string to enter card data
                # entry = "insert into Cards values ('{}'".format(str(json_obj[header_list[0]]))
                # for key in key_list[1:]:
                    # if key in json_obj :
                        # temp = str(json_obj[key]).replace("'", "`").replace('"', '``')
                        # temp = temp.replace('{', '').replace('}', '')
                        # entry += ", '{}'".format(temp)
                    # else:
                        # entry += ", NULL"
                # entry += ")"
                # entry = "insert into Cards values ('l',`k`,`j`,`i`,`h`,`g`,`f`,`e`,`d`,`c`,`b`,`a`)"
                # print(entry, entry.count('`'), len(key_list))
                # c.execute(entry)

    print(str(count) + " cards processed.")
    return True


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("json-parse called with", len(sys.argv), "args")
        print("Please call with in this format")
        print("\tpython3 json-parse.py json-file")

    else:
        print("parsing" + sys.argv[1])
        print("fields will be divided by ',,,'")
        print("keys/values will be divided by '==='")
        print("any new lines will be converted to '|||'")
        input("start? ")

        json_parse(sys.argv[1])
