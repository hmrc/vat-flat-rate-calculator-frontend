############################################################################
# Base service messages
############################################################################
base.service.title     = Gwirio’ch cyfradd unffurf ar gyfer TAW
base.errorSummary      = Mae gwallau ar y dudalen hon
site.back              = Yn ôl

############################################################################
# Common
############################################################################
common.continue                   = Yn eich blaen
common.year                       = blwyddyn
common.quarter                    = chwarter
common.startAgain                 = Dechrau eto

############################################################################
# Errors
############################################################################
error.required                     = Mae angen llenwi’r maes hwn
error.vatReturnPeriod.required     = Dewiswch gyfnod Ffurflen TAW
error.turnover.required            = Nodwch eich trosiant ar gyfer y {0}, gan gynnwys TAW mewn punnoedd
error.costOfGoods.required         = Nodwch gost eich nwyddau ar gyfer y {0}, gan gynnwys TAW mewn punnoedd
error.twoDecimalPlaces             = Mae gormod o leoedd degol yn y swm a nodoch
error.negative                     = Nodwch swm sy’n £0 neu’n fwy
error.moreThanMaximumTurnover      = Nodwch swm sy’n llai na £9,999,999,999.98
error.moreThanMaximumCostOfGoods   = Nodwch swm sy’n llai na £9,999,999,999.98
error.invalidNumber                = Nodwch werth rhifol o hyd at ddau le degol

############################################################################
# Technical errors
############################################################################
techError.title                    = Mae’n ddrwg gennym, ond mae problem gyda’r gwasanaeth
techError.heading                  = Mae’n ddrwg gennym, ond mae problem gyda’r gwasanaeth
techError.para.1                   = Rhowch gynnig arall arni yn nes ymlaen. Nid yw’r broblem hon yn effeithio ar eich cofrestriad am Gyfradd Unffurf ar gyfer TAW (VFR).

############################################################################
# Timeout
############################################################################
timeout.title                      = Mae’r sesiwn wedi dod i ben o ganlyniad i anweithgarwch
timeout.heading                    = Mae problem wedi codi
timeout.para.1                     = Mae’ch sesiwn wedi dod i ben.
timeout.para.2                     = Os ydych yn defnyddio’r Cynllun Cyfradd Unffurf ar gyfer TAW ar hyn o bryd, neu’n meddwl am ymuno â’r cynllun hwn, dechreuwch eto.
timeout.button.message             = Dechrau eto
timeout.message                    = Er eich diogelwch, byddwn yn dileu’ch atebion a chewch eich allgofnodi ymhen
timeout.continue                   = Cael 15 munud arall
timeout.exit                       = Allgofnodi nawr

############################################################################
# VAT return period page
############################################################################
vatReturnPeriod.heading             = Pa mor aml ydych chi’n cyflwyno’ch Ffurflenni TAW?
vatReturnPeriod.para.1              = Os ydych yn defnyddio’r Cynllun Cyfradd Unffurf, dewiswch y cyfnod sy’n cyd-fynd â’ch Ffurflen TAW.
vatReturnPeriod.para.2              = Os ydych yn meddwl am ymuno â’r Cynllun Cyfradd Unffurf, dewiswch ‘bob blwyddyn’.
vatReturnPeriod.option.annually     = Bob blwyddyn
vatReturnPeriod.option.quarterly    = Bob chwarter

############################################################################
# Turnover page
############################################################################
turnover.heading                    = Beth oedd eich trosiant ar gyfer y {0}, gan gynnwys TAW mewn punnoedd?
turnover.para.1                     = Dyma gyfanswm gwerthiannau’r holl nwyddau a gwasanaethau. Os ydych yn amcangyfrif, rhowch ffigurau realistig.

############################################################################
# Cost of goods page
############################################################################
costOfGoods.heading                 = Beth oedd cost eich nwyddau ar gyfer y {0}, gan gynnwys TAW mewn punnoedd?
costOfGoods.para.1                  = Mae ‘nwyddau’ yn ddeunyddiau neu’n eitemau symudol a ddefnyddir at ddibenion eich busnes yn unig. Gallwch hefyd gynnwys nwy a thrydan. Os ydych yn amcangyfrif, rhowch ffigurau realistig.
costOfGoods.doNotInclude            = Peidiwch â chynnwys:
costOfGoods.bullet.1                = unrhyw wasanaethau – hynny yw, unrhyw bethau sydd ddim yn nwyddau
costOfGoods.bullet.2                = treuliau, megis teithio a llety
costOfGoods.bullet.3                = bwyd a diod wedi’u bwyta gennych chi neu’ch staff
costOfGoods.bullet.4                = costau cerbyd, gan gynnwys tanwydd, oni bai eich bod yn y busnes trafnidiaeth ac yn defnyddio’ch cerbyd eich hunain, neu gerbyd ar brydles
costOfGoods.bullet.5                = rhent, llog, biliau ffôn a ffioedd cyfrifyddiaeth
costOfGoods.bullet.6                = rhoddion, eitemau hyrwyddol a chyfraniadau
costOfGoods.bullet.7                = nwyddau y byddwch yn eu hailwerthu neu’n eu rhoi ar log, oni bai mai dyma brif weithgaredd eich busnes
costOfGoods.bullet.8                = hyfforddiant ac aelodaethau
costOfGoods.bullet.9                = eitemau cyfalaf, er enghraifft offer swyddfa, gliniaduron, ffonau symudol a llechi
costOfGoods.para.2                  = Gallwch gael rhagor o fanylion ynghylch <a href={0} class="govuk-link" rel="noreferrer noopener" target="_blank">y Cynllun Cyfradd Unffurf ar gyfer TAW (yn agor tab newydd)</a> gan gynnwys enghreifftiau o nwyddau, gwasanaethau a pha mor aml i ddefnyddio’r offeryn hwn.

############################################################################
# Result page
############################################################################
result.title                        = Eich cyfrifiad TAW

result.1.heading                    = Gallwch ddefnyddio’r gyfradd unffurf ar gyfer TAW o 16.5%
result.2.heading                    = Gallwch ddefnyddio’r gyfradd unffurf ar gyfer TAW o ran y math o fusnes sydd gennych

result.1.summary                    = Ar sail eich atebion, rydych yn fusnes cost gyfyngedig. Mae hyn oherwydd bod costau’ch nwyddau ar gyfer y flwyddyn hon o dan £1,000.
result.2.summary                    = Ar sail eich atebion, rydych yn fusnes cost gyfyngedig. Mae hyn oherwydd bod costau’ch nwyddau yn cyfrif am lai na 2% o’ch trosiant.
result.3.summary                    = Ar sail eich atebion, mae’ch costau yn cyfrif am 2% neu’n fwy o’ch trosiant. Defnyddiwch <a href = {0} class="govuk-link">y gyfradd unffurf ar gyfer TAW o ran y math o fusnes sydd gennych,</a> nid y gyfradd safonol o 16.5% ar gyfer busnes cost gyfyngedig.
result.4.summary                    = Ar sail eich atebion, rydych yn fusnes cost gyfyngedig. Mae hyn oherwydd bod costau’ch nwyddau ar gyfer y chwarter hwn o dan £250.
result.5.summary                    = Ar sail eich atebion, rydych yn fusnes cost gyfyngedig. Mae hyn oherwydd bod costau’ch nwyddau ar gyfer y flwyddyn hon yn cyfrif am lai na 2% o’ch trosiant.
result.6.summary                    = Ar sail eich atebion, mae’ch costau yn cyfrif am 2% neu’n fwy o’ch trosiant. Defnyddiwch <a href = {0} class="govuk-link">y gyfradd unffurf ar gyfer TAW o ran y math o fusnes sydd gennych,</a> nid y gyfradd safonol o 16.5% ar gyfer busnes cost gyfyngedig.

result.para.1                       = Mae’n bosibl y bydd angen i chi wneud y cyfrifiad hwn ar gyfer pob cyfnod TAW
result.accordion.question           = A ydych o fewn blwyddyn gyntaf eich cofrestriad TAW?
result.accordion.summary            = Rhowch ostyngiad o 1% ym mlwyddyn gyntaf eich cofrestriad TAW.
result.heading.2                    = Beth sy’n digwydd nesaf?
result.para.2                       = Defnyddiwch y gyfradd hon o 1 Ebill 2017 wrth i chi gyflwyno’ch Ffurflen TAW.
result.para.3                       = Os ydych yn cyflwyno’ch Ffurflen TAW <a class="govuk-link" href="{0}" rel="external" target="_blank">ewch i’ch cyfrif busnes (yn agor tab newydd)</a>
result.para.4                       = Mae’n bosibl yr hoffech <a href="{0}" class="govuk-link" rel="noreferrer noopener" target="_blank">ymuno â’r Cynllun Cyfradd Unffurf (yn agor tab newydd) neu ei adael</a>
result.para.5                       = Mae’n bosibl yr hoffech <a href="{0}" class="govuk-link" rel="noreferrer noopener" target="_blank">ddatgofrestru rhag TAW (yn agor tab newydd)</a>
result.feedbackSurvey               = Beth oedd eich barn am y gwasanaeth hwn?


result.dismissBanner = Dim diolch
result.dismissBanner.screenreader = Dydw i ddim eisiau ateb cwestiynau ynglŷn ag ymuno â’r panel ymchwil
result.userResearchLink.title = Helpwch i wella GOV.UK
result.userResearchLink.link = Helpwch i wella’r gwasanaeth digidol hwn drwy ymuno â phanel defnyddwyr CThEF (yn agor tab newydd)
result.userResearchLink.text = Mae’r cysylltiad hwn yn agor tab newydd. Sylwer – nid yw’r gwasanaeth hwn ar gael yn Gymraeg.

service.name = Gwirio’ch cyfradd unffurf ar gyfer TAW
site.title.error = Gwall: {0}