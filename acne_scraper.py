import scrapy
import json
import random
from scrapy.selector import HtmlXPathSelector

class AcneSpider(scrapy.Spider):
    name = "acne"
    start_urls = [
        'http://www.skinacea.com/acne/acne-treatment-list.html#AHA'
    ]

    def parse(self, response):
        treatments = []
        descriptions = []
        hxs = scrapy.Selector(response)
        treatments_query = """
                    //h2[a]//text()
                    """
        for line in hxs.xpath(treatments_query).extract():
            if "Acne Treatment Options" in line or "ACNE FREE IN 3 DAYS" in line or "WITCH HAZEL" in line:
                continue
            else:
                treatments.append(line)
        for treatment in list(treatments):
            descriptions_query = '//h2[a[.="' + treatment + '"]]/following-sibling::p[1]//text()'
            result = " ".join(hxs.xpath(descriptions_query).extract())
            if "NOTE: Please consult this far more detailed section" in result:
                descriptions_query = '//h2[a[.="' + treatment + '"]]/following-sibling::p[2]//text()'
                result = " ".join(hxs.xpath(descriptions_query).extract())
            if "Coming soon" in result:
                treatments.remove(treatment)
                continue
            result = result.replace(" .", ".")
            result = result.replace("  ", " ")
            descriptions.append(result)
        
        assert len(descriptions) == len(treatments)

        f = open("./tutorial/spiders/acneed.json", "r")
        db = json.load(f)

        i = 0
        while i != len(descriptions):
            for user in db["users"]:
                if "suggestions" not in db["users"][user]:
                    db["users"][user]["suggestions"] = []
                suggestion = self.make_suggestion(i, treatments, descriptions, db["users"][user]["username"])
                db["users"][user]["suggestions"].append(suggestion)
                db["suggestions"][suggestion["id"]] = suggestion
                i += 1
                if i == len(descriptions):
                    break
        g = open("test.json", "wb")
        json.dump(db, g, indent=4)
        g.close()

    def make_suggestion(self, index, treatments, descriptions, username):
        return {
            "downVotes": 0,
            "user": username,
            "title": treatments[index],
            "upVotes": 0,
            "id": "" + str(random.randint(1<<10, 1<<32)),
            "creationDate": {
                "year": 117,
                "date": 21,
                "time": 1492799891447,
                "hours": 14,
                "seconds": 11,
                "minutes": 38,
                "month": 3,
                "timezoneOffset": 240,
                "day": 5
            },
            "details": descriptions[index]
        }